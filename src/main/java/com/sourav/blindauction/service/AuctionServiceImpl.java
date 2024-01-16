package com.sourav.blindauction.service;

import com.sourav.blindauction.model.Bid;
import com.sourav.blindauction.model.Product;
import com.sourav.blindauction.model.User;
import com.sourav.blindauction.repository.BidRepository;
import com.sourav.blindauction.repository.ProductRepository;
import com.sourav.blindauction.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class AuctionServiceImpl implements AuctionService {

    private static final String EMAIL_SUBJECT = "Congratulations! You won the auction!";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private KafkaEmailService kafkaEmailService;

    @Value("${kafka.email.topic}")
    private String emailTopic;

    @Override
    @Transactional
    public String registerProduct(String sellerToken, Product product) {
        User seller = validateSellerToken(sellerToken);
        if (seller != null) {
            product.setSellerId(seller.getId());
            productRepository.save(product);
            return "Product registered successfully";
        } else {
            return "Invalid seller token";
        }
    }

    @Override
    @Transactional
    public boolean placeBid(String buyerToken, String productId, Bid bid) {
        User buyer = validateBuyerToken(buyerToken);
        Product product = productRepository.findById(productId).orElse(null);

        if (buyer != null && product != null && bid.getBidAmount() >= product.getMinBidPrice()) {
            bid.setBuyerId(buyer.getId());
            bid.setProductId(productId);
            bidRepository.save(bid);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public String closeAuction(String sellerToken, String productId) {
        User seller = validateSellerToken(sellerToken);
        Product product = productRepository.findById(productId).orElse(null);

        if (seller != null && product != null) {
            Bid winningBid = bidRepository.findTopByProductIdOrderByBidAmountDesc(productId);

            if (winningBid != null) {
                String winnerId = winningBid.getBuyerId();
                double winningBidAmount = winningBid.getBidAmount();
                User winner = userRepository.findById(winnerId).orElse(null);

                if (winner != null) {
                    notifySellerByEmail(productId, winningBidAmount, winner);
                }

                product.setAuctionClosed(true);
                productRepository.save(product);

                return "Auction closed successfully. Winner: " + winnerId + ", Winning Bid: " + winningBidAmount;
            } else {
                return "No valid bids for the product";
            }
        } else {
            return "Invalid seller token or product ID";
        }
    }

    private void notifySellerByEmail(String productId, double winningBidAmount, User winner) {
        String emailBody = "You are the winner of the auction for product " + productId +
                ". Winning Bid: " + winningBidAmount;

        try {
            // Notify the winner via Kafka message for email notification
            kafkaEmailService.sendEmailNotification(winner.getEmail(), EMAIL_SUBJECT, emailBody);
        } catch (Exception e) {
            log.error("Exception Occurred while sending email ", e);
        }
    }

    private User validateSellerToken(String sellerToken) {
        return userRepository.findByToken(sellerToken);
    }

    private User validateBuyerToken(String buyerToken) {
        return userRepository.findByToken(buyerToken);
    }
}