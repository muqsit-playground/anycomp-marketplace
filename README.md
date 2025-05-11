# AnyComp Marketplace
A simple marketplace project that implements business logic using Java Springboot.
This project implements 4 entities: a `Buyer`, a `Seller`, an `Item`, and a `Purchase`.

## Building
Prerequisites:
- Java **17+**
- Maven
- PostgreSQL (17.x) (although, you can modify the JDBC URI in `application.properties` for other databases)

1. Build the project and download dependencies:
   ```
   mvn clean install
   ```
2. Launch the application
   ```
   mvn spring-boot:run
   ```
3. ✔**️Done**. Navigate to the endpoint at http://localhost:8080 to verify the application is working. Swagger UI will be available at http://localhost:8080/swagger-ui/index.html.

## REST endpoints
Entity records can be managed using the following REST endpoints:

| **Method** | **Endpoint**   | **Description**                    |
| ---------- | -------------- | ---------------------------------- |
| **GET**    | `/buyers`      | List all buyers (paged + sortable) |
| **GET**    | `/buyers/{id}` | Retrieve a buyer by ID      |
| **POST**   | `/buyers`      | Create new buyer                 |
| **PUT**    | `/buyers/{id}` | Update existing buyer           |
| **DELETE** | `/buyers/{id}` | Delete a buyer                     |
| **GET**    | `/sellers`      | List all sellers (paged + sortable) |
| **GET**    | `/sellers/{id}` | Retrieve a seller by ID      |
| **POST**   | `/sellers`      | Create new seller                 |
| **PUT**    | `/sellers/{id}` | Update existing seller           |
| **DELETE** | `/sellers/{id}` | Delete a seller                     |
| **GET**    | `/items`                    | List all items (paged + sortable)    |
| **GET**    | `/items/{id}`               | Retrieve an item by ID               |
| **GET**    | `/sellers/{sellerId}/items` | List items sold by some seller |
| **POST**   | `/sellers/{sellerId}/items` | Add new item to seller           |
| **PUT**    | `/items/{id}`               | Update an item                       |
| **DELETE** | `/items/{id}`               | Delete an item                       |
| **POST**   | `/purchase`  | Purchase item as some buyer (creates a `Purchase` record and checks stock and deducts quantity) |
