# Meeting Room Manager

A Spring Boot application for managing meeting rooms and reservations in an organization.

![Meeting Room Manager](https://img.shields.io/badge/Spring%20Boot-3.0.0-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17.4-blue)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1.3-orange)

## 📋 Overview

Meeting Room Manager is a web-based application that helps organizations efficiently manage their meeting spaces and room reservations. The application features role-based access control, allowing administrators to manage rooms while regular users can view rooms and make reservations.

## ✨ Features

- **User Authentication & Authorization**
  - Secure login and registration
  - Role-based access control (Admin and User roles)
  - Custom access denied handling with user-friendly messages

- **Room Management (Admin only)**
  - Create, edit, and delete meeting rooms
  - Specify room capacity and location

- **Reservation System**
  - Create reservations for specific time slots
  - View all reservations in the system
  - Manage your own reservations

- **Responsive UI**
  - Modern, Bootstrap-based interface
  - Mobile-friendly design
  - Intuitive navigation

## 🛠️ Technologies Used

- **Backend**
  - Spring Boot
  - Spring Security
  - Spring Data JPA
  - Hibernate

- **Frontend**
  - Thymeleaf
  - Bootstrap 5
  - Font Awesome

- **Database**
  - PostgreSQL

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL 12 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/meeting-room-manager.git
   cd meeting-room-manager
   ```

2. **Configure the database**
   
   Update `src/main/resources/application.properties` with your PostgreSQL credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/meeting_room_manager
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access the application**
   
   Open your browser and navigate to: `http://localhost:8080`

### Default Credentials

The application comes with a pre-configured admin user:
- Username: `admin`
- Password: `admin123`

## 🧩 Project Structure

```
meeting-room-manager/
├── src/main/java/com/mrm/meetingroommanager/
│   ├── config/                  # Configuration classes
│   ├── controllers/             # MVC controllers
│   ├── entities/                # JPA entities
│   ├── repositories/            # Spring Data repositories
│   ├── services/                # Business logic services
│   └── MeetingRoomManagerApplication.java
├── src/main/resources/
│   ├── templates/               # Thymeleaf templates
│   ├── static/                  # Static resources (CSS, JS)
│   └── application.properties   # Application configuration
└── pom.xml                      # Maven dependencies
```

## 🔐 Security

The application implements Spring Security with the following features:
- BCrypt password encoding
- Role-based access control
- Custom access denied handling
- CSRF protection (disabled for API endpoints)

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Contributors

- Your Name - Initial work

## 🙏 Acknowledgements

- Spring Boot team for the excellent framework
- Bootstrap team for the responsive UI components
- PostgreSQL for the reliable database system
