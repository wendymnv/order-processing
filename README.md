
### Description

This project is a Spring Boot application that integrates customer and product details into an order. It uses reactive programming with Project Reactor to handle asynchronous operations. The application includes services for fetching customer and product details and combines them into an order.

### Steps to Deploy the Project

1. **Clone the Repository**
   ```sh
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Build the Project**
   Ensure you have Maven installed. Run the following command to build the project:
   ```sh
   mvn clean install
   ```

3. **Run the Application**
   Use the following command to run the Spring Boot application:
   ```sh
   mvn spring-boot:run
   ```

4. **Access the Application**
   Once the application is running, you can access it at:
   ```
   http://localhost:8080
   ```

5. **Run Tests**
   To run the unit tests, use the following command:
   ```sh
   mvn test
   ```

### Prerequisites

- Java 21 or higher
- Maven 3.6.0 or higher

### Configuration

Ensure you have the necessary configuration in `application.properties` or `application.yml` for any external services or databases the application depends on.

### Additional Information

For more details , refer to  the source code.