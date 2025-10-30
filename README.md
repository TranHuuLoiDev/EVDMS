# EV Dealer - Customer Module (Backend API)

This project provides REST APIs for Customers, Test Drives and Feedbacks based on existing database schema `ev_dealer_management_db`.

## Quick start

1. Make sure MySQL is running and you have created the database `ev_dealer_management_db`. You can import the provided SQL file (`ev_dealer_management_db.sql`) into your MySQL server.
2. Configure MySQL credentials in `src/main/resources/application.properties` if different.
3. Build and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
4. APIs:
   - `GET /api/customers`
   - `GET /api/customers/{id}`
   - `POST /api/customers`
   - `PUT /api/customers/{id}`
   - `DELETE /api/customers/{id}`
   - Similar endpoints for `/api/testdrives` and `/api/feedbacks`

Note: `spring.jpa.hibernate.ddl-auto=none` because database schema is provided.
