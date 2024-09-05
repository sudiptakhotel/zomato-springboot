# ZomatoApp

ZomatoApp is a backend service for a food delivery application, inspired by Zomato. This application is built using the Spring Boot framework and follows a monolithic architecture. The backend handles various functionalities such as user management, restaurant management, order processing, and more.

## Features

- **User Management:** signup, login, and profile management for customers and partners.
- **Restaurant Management:** Add, update, and manage restaurant details, menus, and working hours.
- **Order Management:** Customers can place orders,cancel orders , track status, and view order history.
- **Payment Management:** Integration with payment services , which supports wallet and cash payment for processing.
- **Promotions and Discounts:** Apply promos and discounts during order placement.
- **Email Notifications:** Sends order details and status updates via email.
- **SMS Notifications:** Uses Twilio for sending SMS updates.
- **Real-Time Order Tracking:** Allows customers to track their orders in real-time.
- **Security:** JWT-based authentication and role-based access control.

## Technologies Used

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security**
- **Hibernate**
- **PostgreSQL**
- **Thymeleaf**
- **Gmail SMTP**
- **Twilio**
- **Swagger/OpenAPI**
- **Docker**
- **JUnit & Mockito**

## Flow Diagram (LLD)
- https://whimsical.com/zomato-application-lld-FFcEeXrsTKhQ8VFMAbqCKQ

### Installation

1. **Clone the repository:**

   ```bash
   https://github.com/sudiptakhotel/zomato-springboot.git
   cd ZomatoApp
