# blind-auction
This project is based on the algorithm given in https://en.wikipedia.org/wiki/First-price_sealed-bid_auction

Sample requests (REST with endpoints) and responses. Used Postman to test the requests
1. Register User:
   Method: POST
   URL: http://localhost:8080/auction/users/register
   Request Body:
   {
   ""id": 213,
   "username": "john_doe",
   "email": "john@example.com",
   "address": "Berlin"
   }
   Response: User registered successfully: db7dc785-f665-4d1a-9e34-a0e9e92bc847
2. Validate Token:
   http://localhost:8080/auction/users/validate-token?token=db7dc785-f665-4d1a-9e34-a0e9e92bc847
3. Register Product:
   1. Request Body:
      {
      "id": "product101",
      "sellerId": "user101",
      "productName": "iPhone 14 Pro",
      "minBidPrice": 500.00,
      "auctionClosed": false
      }
   2. Add Header Authorization: db7dc785-f665-4d1a-9e34-a0e9e92bc847 in Header section of Postman
      Response: Product registered successfully
4. Place Bid:
   1. URL: http://localhost:8080/auction/bid?productId=product101
   2. Request Body:
      {
      "id": "bid101",
      "bidAmount": 600.00
      }
   3. Add Header Authorization: db7dc785-f665-4d1a-9e34-a0e9e92bc847 in Header section of Postman
   Response: true
5. Close Auction:
   1. URL: http://localhost:8080/auction/close?productId=product101
   2. Add Header Authorization: db7dc785-f665-4d1a-9e34-a0e9e92bc847 in Header section of Postman
   


