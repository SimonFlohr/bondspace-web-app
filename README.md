# BondSpace - Full-Stack Web Application

*BondSpace is a web application designed to help people create collaborative spaces where they can share and preserve memories together.*

## Overview

**Purpose**: BondSpace was created to provide a secure, private platform for groups to document and share important moments, thoughts, and experiences.

**Scope**: The application supports space creation, user invitations, memory creation with tags, and collaborative content management within spaces.

## Features

- **User Authentication:** Secure login and registration system
- **Space Management:** Create, join, and manage collaborative spaces
- **Memory Creation:** Add text-based memories with custom tags
- **Collaborative Features:** Invite members to spaces and share memories
- **Notification System:** Real-time notifications for space invitations
- **Search Functionality:** Search and filter memories by various criteria
- **Responsive Design:** Works across desktop and mobile devices

## Tech Stack

- **Frontend:** Angular 16+, Bootstrap 5, TypeScript, RxJS
- **Backend:** Spring Boot 3, Spring Security, Spring Data JPA, Jakarta EE
- **Database:** PostgreSQL
- **Containerization:** Docker

## Prerequisites

To run BondSpace locally and containerized, you will need:
- [Docker](https://www.docker.com/get-started) (version 20.X or above)

Since the application if fully containerized, you do not need to install Node.js, Java, Maven, or PostgreSQL locally.

**Note**: If you are planning on developing and/or building the application components individually without Docker, you will need:
- [Node.js](https://nodejs.org/) (version 16.x or above)
- [Angular CLI](https://angular.io/cli) (version 16.x or above)
- [Java](https://www.oracle.com/java/) (JDK 17 or above)
- [Maven](https://maven.apache.org/) (version 3.8.x or above)
- [PostgreSQL](https://www.postgresql.org/) (version 14.x or above)

## Installation

### Containerized

**1. Clone the Repository**

```bash
# Clone the repository
git clone https://github.com/SimonFlohr/bondspace-web-app.git
cd bondspace-web-app
```

**2. Build and Run the Docker Containers**

```bash
# Method 1: Run the rebuild and run script
./build-docker.sh

# Method 2: Build and run the containers manually
docker-compose up --build --force-recreate
```

**3. Access the Web Interface**

After building and running the Docker containers, access the web interface by visiting `http://localhost` in your web browser.

### Non-Containerized (for development)

**1. Clone the Repository**

```bash
# Clone the repository
git clone https://github.com/SimonFlohr/bondspace-web-app.git
cd bondspace-web-app
```

**2. Build and Run the Spring Boot Backend**

```bash
# Navigate to the backend directory
cd backend

# Build the Spring Boot application
mvn clean package -DskipTests

# Run Spring Boot
mvn spring-boot:run
```

**3. Build and Run the Angular Frontend**

```bash
# Navigate to the frontend directory
cd frontend/angular-bondspace-web-app

# Install dependencies
npm install

# Build the Angular application
ng build --configuration production

# Run Angular
ng serve
```

**4. Access the Web Interface**

After building and running the frontend and backend, access the web interface by visiting `http://localhost:4200` in your web browser.

## Usage

After starting the application:

1. Access the web interface at `http://localhost` (or `http://localhost:4200` if not dockerized)
2. Register a new account
3. Create a new space or accept invitations to existing spaces
4. Start creating and sharing memories within your spaces

## AWS Architecture

BondSpace is deployed on AWS at https://bondspace.app, using the following architecture:

1. **Domain & DNS:**
    - Domain registered with GoDaddy: `bondspace.app`
    - DNS managed through AWS Route 53
    - SSL certificate provisioned through AWS Certificate Manager
2. **Container Registry:**
    - Frontend and backend Docker images stored in Amazon ECR
3. **Compute:**
    - Containers deployed to Amazon ECS
    - Load balancing through Application Load Balancer
4. **Database:**
    - PostgreSQL database hosted on Amazon RDS
5. **Network:**
    - VPC with public and private subnets
    - Security groups controlling access

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgements

- [Angular](https://angular.io/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Bootstrap](https://getbootstrap.com/)
- [Docker](https://www.docker.com/)
- [AWS](https://aws.amazon.com/)

## Contact

Simon Flohr
Full-Stack Software Engineer
simonf.swe@gmail.com

Project Link: https://github.com/SimonFlohr/bondspace-web-app