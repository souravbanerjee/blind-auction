package com.sourav.blindauction.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

import com.sourav.blindauction.model.Bid;
import com.sourav.blindauction.model.Product;
import com.sourav.blindauction.model.User;
import com.sourav.blindauction.repository.BidRepository;
import com.sourav.blindauction.repository.ProductRepository;
import com.sourav.blindauction.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuctionServiceImplTest {

    @InjectMocks
    private AuctionServiceImpl auctionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BidRepository bidRepository;

    @Mock
    private KafkaEmailService kafkaEmailService;

    @Test
    public void testRegisterProduct_ValidSellerToken_ReturnsSuccessMessage() {
        String sellerToken = "validSellerToken";
        Product product = new Product();
        User mockSeller = new User();
        mockSeller.setId("sellerId");

        when(userRepository.findByToken(sellerToken)).thenReturn(mockSeller);

        String result = auctionService.registerProduct(sellerToken, product);

        verify(productRepository, times(1)).save(product);
        assertEquals("Product registered successfully", result);
    }

    @Test
    public void testRegisterProduct_InvalidSellerToken_ReturnsErrorMessage() {
        String sellerToken = "invalidSellerToken";
        Product product = new Product();

        when(userRepository.findByToken(sellerToken)).thenReturn(null);

        String result = auctionService.registerProduct(sellerToken, product);

        verify(productRepository, never()).save(product);
        assertEquals("Invalid seller token", result);
    }

    @Test
    @Transactional
    public void testPlaceBid_ValidBid_ReturnsTrue() {
        String buyerToken = "validBuyerToken";
        String productId = "productId";
        Bid bid = new Bid();
        bid.setBidAmount(150.0);

        User mockBuyer = new User();
        mockBuyer.setId("buyerId");
        when(userRepository.findByToken(buyerToken)).thenReturn(mockBuyer);

        Product mockProduct = new Product();
        mockProduct.setMinBidPrice(100.0);
        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(mockProduct));

        boolean result = auctionService.placeBid(buyerToken, productId, bid);
        verify(bidRepository, times(1)).save(bid);
        assertTrue(result);
    }

    @Test
    public void testCloseAuction_ValidSellerAndProduct_WinnerNotifiedAndAuctionClosed() throws ExecutionException, InterruptedException {

        String sellerToken = "validSellerToken";
        String productId = "validProductId";

        User mockSeller = new User();
        mockSeller.setId("sellerId");
        when(userRepository.findByToken(sellerToken)).thenReturn(mockSeller);

        Product mockProduct = new Product();
        mockProduct.setAuctionClosed(false);
        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(mockProduct));

        Bid mockWinningBid = new Bid();
        mockWinningBid.setBuyerId("winnerId");
        mockWinningBid.setBidAmount(200.0);
        when(bidRepository.findTopByProductIdOrderByBidAmountDesc(productId)).thenReturn(mockWinningBid);

        User mockWinner = new User();
        mockWinner.setEmail("winner@example.com");
        when(userRepository.findById("winnerId")).thenReturn(java.util.Optional.of(mockWinner));

        String result = auctionService.closeAuction(sellerToken, productId);

        verify(kafkaEmailService, times(1)).sendEmailNotification(eq("winner@example.com"), anyString(), anyString());
        assertTrue(mockProduct.isAuctionClosed());
        assertEquals("Auction closed successfully. Winner: winnerId, Winning Bid: 200.0", result);
    }

    @Test
    public void testCloseAuction_NoValidBids_ReturnsNoBidsMessage() throws ExecutionException, InterruptedException {

        String sellerToken = "validSellerToken";
        String productId = "validProductId";

        User mockSeller = new User();
        mockSeller.setId("sellerId");
        when(userRepository.findByToken(sellerToken)).thenReturn(mockSeller);

        Product mockProduct = new Product();
        mockProduct.setAuctionClosed(false);
        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(mockProduct));

        when(bidRepository.findTopByProductIdOrderByBidAmountDesc(productId)).thenReturn(null);

        String result = auctionService.closeAuction(sellerToken, productId);
        verify(kafkaEmailService, never()).sendEmailNotification(anyString(), anyString(), anyString());
        assertFalse(mockProduct.isAuctionClosed());
        assertEquals("No valid bids for the product", result);
    }

    @Test
    public void testCloseAuction_InvalidSellerOrProduct_ReturnsErrorMessage() throws ExecutionException, InterruptedException {

        String sellerToken = "invalidSellerToken";
        String productId = "invalidProductId";

        when(userRepository.findByToken(sellerToken)).thenReturn(null);
        when(productRepository.findById(productId)).thenReturn(java.util.Optional.empty());

        String result = auctionService.closeAuction(sellerToken, productId);
        verify(kafkaEmailService, never()).sendEmailNotification(anyString(), anyString(), anyString());
        assertEquals("Invalid seller token or product ID", result);
    }


}
