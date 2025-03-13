# User Management API

## Prerequisites

Before running the application, ensure you have the following installed on your machine:

- **Java 8** 
- **Maven**
- **Docker** (for running PostgreSQL in a container)

## Set Up PostgreSQL Database with Docker

1. **Pull the PostgreSQL image** and run the container with the following command:

    ```bash
    docker run --name postgres-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=management_db -p 5432:5432 -d postgres:latest
    ```

    This will:
    - Create a PostgreSQL container with the name `postgres-db`.
    - Set the database username to `admin`, the password to `admin`, and the database name to `management_db`.
    - Feel free to user other credentials but make sure you update application.yaml to the new credentials
    - Map port `5432` on your local machine to port `5432` inside the container.
    - Feel free to use other ports but make sure you update the ports in application.yaml also

2. Verify the container is running with:

    ```bash
    docker ps
    ```

    You should see the PostgreSQL container running.

## Run the Application

1. **Clone the Repository** to your local machine:

    ```bash
    git clone https://github.com/marcofig98/management-repository.git
    cd your-repository
    ```

2. **Build the application** using Maven:

    ```bash
    ./mvnw clean install
    ```

3. **Run the application** with the following command:

    ```bash
    ./mvnw spring-boot:run
    ```

    The application will start running on `http://localhost:8080`.
    feel free to change the port "8080" to another in the application.yaml, if you do it you should update the port in swagger url also

## Access Swagger UI

Once the application is running, you can access the **Swagger UI** at: http://localhost:8080/swagger-ui.html
Here you can explore and interact with the available endpoints of the API.




