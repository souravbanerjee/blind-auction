package com.sourav.blindauction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourav.blindauction.model.Bid;
import com.sourav.blindauction.model.Product;
import com.sourav.blindauction.model.User;
import com.sourav.blindauction.service.AuctionService;
import com.sourav.blindauction.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AuctionController.class)
public class AuctionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuctionService auctionService;

    @Test
    public void testRegisterUser() throws Exception {
        User user = new User();

        Mockito.when(userService.registerUser(Mockito.any(User.class))).thenReturn("User registered successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/auction/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User registered successfully"));
    }

    @Test
    public void testValidateToken() throws Exception {
        String token = "testToken";

        Mockito.when(userService.validateToken(Mockito.anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/auction/users/validate-token")
                .param("token", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    public void testRegisterProduct() throws Exception {
        String sellerToken = "sellerToken";
        Product product = new Product(/* initialize product here */);

        Mockito.when(auctionService.registerProduct(Mockito.anyString(), Mockito.any(Product.class)))
                .thenReturn("Product registered successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/auction/products/register")
                .header("Authorization", sellerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(product)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Product registered successfully"));
    }

    @Test
    public void testPlaceBid() throws Exception {
        String buyerToken = "buyerToken";
        String productId = "testProductId";
        Bid bid = new Bid(/* initialize bid here */);

        Mockito.when(auctionService.placeBid(Mockito.anyString(), Mockito.anyString(), Mockito.any(Bid.class)))
                .thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/auction/bid")
                .header("Authorization", buyerToken)
                .param("productId", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bid)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    public void testCloseAuction() throws Exception {
        String sellerToken = "sellerToken";
        String productId = "testProductId";

        Mockito.when(auctionService.closeAuction(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("Auction closed successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/auction/close")
                .header("Authorization", sellerToken)
                .param("productId", productId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Auction closed successfully"));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
