# FarMket – Farmer-to-Buyer Agricultural Marketplace

## 1. Project Overview

**FarMket** is a web-based agricultural marketplace designed to connect farmers directly with buyers. The platform aims to simplify the process of selling and purchasing agricultural products by providing a common digital platform.

Farmers can register, log in, and manage their agricultural products. Buyers can browse available products and send enquiries to farmers. The platform aims to improve communication between farmers and buyers and support a more transparent agricultural supply chain.

## 2. Objectives

- Connect farmers directly with buyers.
- Provide a platform for listing agricultural products.
- Allow buyers to discover products and contact farmers through enquiries.
- Enable farmers to manage and respond to buyer enquiries.
- Store application data in a database.
- Develop the application using Java concepts suitable for a second-year computer science student.

## 3. Technologies Used

| Technology | Purpose |
|---|---|
| Java 21 | Main programming language |
| Spring Boot 3.5.6 | Backend application framework |
| Thymeleaf | Server-side HTML rendering |
| HTML5 | Webpage structure |
| CSS3 | Website styling and layout |
| MongoDB Atlas | Cloud database |
| Spring Data MongoDB | Database access and repository operations |
| Maven | Project dependency management and build tool |
| Visual Studio Code | Development environment |

**Note:** The project is designed to use Java for application logic rather than JavaScript.

## 4. Main Features

### User Registration and Login
- Registration for farmers and buyers.
- Login functionality for registered users.
- User information stored in MongoDB Atlas.

### Farmer Dashboard
- Access to farmer-related functionality.
- Manage agricultural product listings.
- View and respond to buyer enquiries.

### Buyer Dashboard
- Access to buyer-related functionality.
- View available agricultural products.
- Send enquiries to farmers about products.

### Enquiry Management
- Buyers can submit enquiries about agricultural products.
- Farmers can view enquiries and respond to them.
- Enquiry information and status can be stored in the database.

## 5. System Architecture

FarMket follows a layered structure using Spring Boot and Spring Data MongoDB.

1. **Presentation Layer:** HTML, CSS, and Thymeleaf templates display the user interface.
2. **Controller Layer:** Spring Boot controllers handle HTTP requests and coordinate application operations.
3. **Model Layer:** Java model classes represent users, products, and enquiries.
4. **Repository Layer:** Spring Data MongoDB repositories provide database operations.
5. **Database Layer:** MongoDB Atlas stores application data.

### Request Flow

User → HTML Form → Spring Boot Controller → Repository → MongoDB Atlas

The controller can then return a Thymeleaf template or redirect the user to another page.

## 6. Project Structure

```text
FarMket-Java/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── farmket/
│       │           ├── FarMketApplication.java
│       │           ├── controllers/
│       │           ├── models/
│       │           └── repositories/
│       └── resources/
│           ├── application.properties
│           ├── static/
│           │   └── css/
│           │       └── style.css
│           └── templates/
│               ├── index.html
│               ├── login.html
│               ├── register.html
│               ├── farmer-dashboard.html
│               ├── buyer-dashboard.html
│               ├── farmer-enquiries.html
│               └── buyer-enquiries.html
├── pom.xml
├── .env
└── README.md
```

*Note: This is a representative structure. Adjust the folder names to match your actual source files.*

## 7. Prerequisites

Install or configure the following before running the application:

- Java Development Kit (JDK) 21 or a compatible version.
- Visual Studio Code.
- MongoDB Atlas account and a configured database cluster.
- Maven, or the Maven distribution included with this project.

## 8. Database Configuration

FarMket uses MongoDB Atlas to store application data.

The MongoDB connection string is configured through an environment variable.

In `src/main/resources/application.properties`:

```properties
spring.data.mongodb.uri=${MONGODB_URI}
```

Create a `.env` file in the project root and add your MongoDB Atlas connection string:

```bash
MONGODB_URI=mongodb+srv://USERNAME:PASSWORD@YOUR-CLUSTER.mongodb.net/farmket
```

Replace the placeholders with your actual Atlas connection details.

**Security precautions:**
- Never commit `.env` to GitHub.
- Never publish your MongoDB password or connection string.
- Ensure `.env` is listed in `.gitignore`.
- Configure your MongoDB Atlas network access and database user appropriately.

Spring Boot does not automatically load a `.env` file by default. Make sure the `MONGODB_URI` environment variable is exported before starting the application.

## 9. How to Run the Project

### Step 1: Open the project

Open the `FarMket-Java` folder in Visual Studio Code.

### Step 2: Configure the database

Set up MongoDB Atlas and configure the `MONGODB_URI` environment variable as described above.

On macOS or Linux, if your `.env` file contains the variable in standard shell-compatible format, run:

```bash
set -a
source .env
set +a
```

### Step 3: Start the application

If Maven is installed globally:

```bash
mvn spring-boot:run
```

If you are using the Maven distribution included in the project:

```bash
./apache-maven-3.10.0/bin/mvn spring-boot:run
```

### Step 4: Open the website

Visit:

http://localhost:8080

If port 8080 is already occupied, stop the existing application or start FarMket on another port.

## 10. Database Models

The project contains model classes representing the main data entities.

| Model | Purpose |
|---|---|
| `User.java` | Represents registered users, including farmers and buyers |
| `Product.java` | Represents agricultural products |
| `Enquiry.java` | Represents enquiries submitted by buyers |

The corresponding repository interfaces provide database operations for these models.

## 11. Future Enhancements

The following features may be considered for future development:

- Product search and category-based filtering.
- Improved product availability and stock management.
- Automatic stock updates after confirmed transactions.
- Farmer and buyer profile management.
- Notifications for new enquiries and enquiry status changes.
- Product price comparison and location-based recommendations.
- Improved form validation and error messages.
- Secure authentication and role-based access control.
- Responsive design for mobile devices.
- Order tracking and transaction management.

These are proposed enhancements and should not be considered completed until implemented and tested.

## 12. Social Impact and Sustainable Development Goals

FarMket aims to support a more accessible and transparent agricultural supply chain.

Potentially relevant United Nations Sustainable Development Goals include:

- **SDG 2 – Zero Hunger:** Supports agricultural market access.
- **SDG 8 – Decent Work and Economic Growth:** Aims to improve market opportunities for farmers.
- **SDG 9 – Industry, Innovation and Infrastructure:** Uses digital technology to support agricultural trade.
- **SDG 12 – Responsible Consumption and Production:** Could support more efficient connections between producers and buyers.

These are intended areas of impact, not claims of measured outcomes.

## 13. Learning Outcomes

This project provides practical experience with:

- Java programming and object-oriented programming.
- Spring Boot application development.
- Model-View-Controller (MVC) architecture.
- HTML and CSS.
- Thymeleaf templates.
- Spring Data MongoDB repositories.
- MongoDB Atlas configuration.
- Maven dependency management.
- Git and GitHub version control.

## 14. Project Status

FarMket is under development. The application includes a Spring Boot backend, Thymeleaf templates, and MongoDB repository integration.

The application should be tested end-to-end to verify registration, login, product management, enquiry processing, and database persistence before being considered production-ready.

## 15. Author

Developed as a student mini-project focused on connecting farmers and buyers through a digital agricultural marketplace.

---

**FarMket – Connecting Farmers and Buyers Through Technology.**
