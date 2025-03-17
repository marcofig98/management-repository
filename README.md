# Order Management API

This is an API for managing product orders in a system where users, items, orders, and stock movements are central entities. It allows users to create and manage users and items, as well as place orders for items they wish to purchase.

## API Description

The Order Management API enables the following functionalities:

- **User and Item Management**: 
  - You can create and manage users and items through the API.
  
- **Order Creation**: 
  - Users can create orders for items, provided the items exist within the system. 
  - If there is no available stock, the order remains in the `PENDING` state until stock becomes available.
  
- **Stock Movement**:
  - Stock movements allow the system to increase the available stock for items. 
  - When stock is available, orders can be processed and completed.

- **Order Completion**:
  - When stock is available for an order, the order is processed, and the user is notified via email about the status of the order.
  - If there is no enough stock, the order remains in the `PENDING` state.

- **Stock Movement Impact**:
  - When stock movements are created, the system checks if any pending orders can be completed with the newly available stock. 
  - If stock is used to complete a pending order, a new stock movement is created with a negative quantity, representing a decrease in stock for the item.

- **Logging**:
  - Actions related to user and item management are logged into a log file.
  - The creation and completion of orders, the creation of stock movements, and the sending of emails are also logged.

## Prerequisites

Before running the application, ensure you have the following installed on your machine:

- **Java 8** 
- **Maven**
- **Docker** (for running PostgreSQL in a container)
- **Gmail account** (for email sending)

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


## Email Sending with Gmail

For email sending to work properly in the application, you need to configure an email account with the domain **@gmail.com**. Follow the steps below to set up your account and enable email sending.

### Step 1: Enable Two-Factor Authentication (2FA) on your Google account

1. Go to your Google account at [https://myaccount.google.com/](https://myaccount.google.com/).
2. Navigate to the **Security** section.
3. Enable **Two-Factor Authentication** (2FA). This is required to generate an app password.

### Step 2: Generate an App Password

1. After enabling Two-Factor Authentication, go to the **App Passwords** section.
2. Select **Generate App Password** and follow the instructions to generate an app password.
3. Copy the generated password, which will be used to authenticate your application when sending emails.

For more details on how to create an app password, you can watch this tutorial video: [How to Create an App Password for Gmail](https://www.youtube.com/watch?v=GsXyF5Zb5UY).

### Step 3: Configure the `application.yaml`

Now, with the generated app password, you need to navigate to `application.yaml` and edit it to use your Gmail account.

1. In the `application.yaml` file, locate the `spring.mail.username` and `spring.mail.password` fields.
2. In the `spring.mail.username` field, enter your **Gmail email address** (e.g., `youremail@gmail.com`).
3. In the `spring.mail.password` field, enter the **app password** generated by Google.
4. Don't change the other fields

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

Once the application is running, you can access the **Swagger UI** at: http://localhost:8080/api/swagger-ui.html
Here you can explore and interact with the available endpoints of the API.


## Testing the Application

To test the application, follow these steps:

### Step 1: Create Users

Use the `POST /users` endpoint to create users. Provide only the `name` and `email` fields when creating a user. You do **not** need to provide an `id` as it will be automatically generated by the application.

#### Request Body Example:

```json
{
  "name": "john",
  "email": "example@outlook.com"
}

### Step 1.1: Update Users (Not Required)

Use the `PATCH /users` endpoint to update users. Provide only the `name` and `email` fields. You do **not** need to provide an `id`.

#### Request Body Example:

```json
{
  "name": "john",
  "email": "example@outlook.com"
}

### Step 2: Create Items

Use the `POST /items` endpoint to create items. You can provide the necessary details of the item, such as `name` and `price`, but do **not** provide an `id` as it will be automatically generated.

#### Request Body Example:

```json
{
  "name": "chair"
}

### Step 2.1: Update Items (Not Required)

Use the `PATCH /items` endpoint to update item name. Provide only the `name` field. You do **not** need to provide an `id`.

#### Request Body Example:

```json
{
  "name": "chair one"
}

### Step 3: Create Stock Movements

Use the `POST /stock-movements` endpoint to create stock movements. You only need to provide the `id` of the item and the `quantity`, but do **not** provide the other fields.

#### Request Body Example:

Id should be replaced with the real id of the item in your system

```json
{
  "item": {
    "id": "78abcd88-c0bf-4bd7-8509-46478981270a"
  },
  "quantity": 1
}

### Step 4: Create Orders

Use the `POST /orders` endpoint to create orders. Provide the `id` of the user who is making the order, the `id` of the item they wish to order, and the `quantity` they want to purchase, but do **not** provide the other fields.

Once an order is placed and completed, the user who made the order will receive an email with the order details.

#### Request Body Example:

Id of the item should be replaced with the real id of the item in your system
Id of the user should be replaced with the real id of the user in your system

```json
{
  "item": {
    "id": "78abcd88-c0bf-4bd7-8509-46478981270a"
  },
  "quantity": 2,
  "user": {
    "id": "8c2eaab2-83ed-48ad-a36b-2ff884987026"
  }
}

### Step 5: Alternate Requests

You can alternate between creating stock movements and placing orders. Each time an order is completed, the corresponding user will receive a confirmation email.

---

By following these steps, you can test the complete flow of creating users, items, stock movements, and orders, while also confirming the email functionality.





