# PropertyApp

Property App with Spring and Next.js.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Installation](#installation)
  - [Backend Setup (Java Spring)](#backend-setup-java-spring)
  - [RabbitMQ Setup](#rabbitmq-setup)
  - [Database Setup (PostgreSQL)](#database-setup-postgresql)
  - [Frontend Setup (Next.js)](#frontend-setup-nextjs)
- [Running the Application](#running-the-application)
  - [Running the Backend](#running-the-backend)
  - [Running the Frontend](#running-the-frontend)
- [Configuration](#configuration)
- [Contributing](#contributing)
- [License](#license)
## Screenshots

<div align="center">
  <table>
    <tr>
      <td>
        <img width="1426" alt="ss1" src="https://github.com/user-attachments/assets/513dd475-48f3-4ce2-af23-99f1bf0e786c">
      </td>
      <td>
        <img width="1426" alt="ss2" src="https://github.com/user-attachments/assets/8f17f480-9bae-4df1-adf0-c2a4ff31ebb5">
      </td>
      <td>
        <img width="1426" alt="ss3" src="https://github.com/user-attachments/assets/5b8bfd19-7e4d-4ea2-8fa5-e2006313e748">
      </td>
      <td>
        <img width="1426" alt="ss4" src="https://github.com/user-attachments/assets/2db6c788-5aa1-44a6-9b81-74249ae31178">
      </td>
      <td>
        <img width="1426" alt="ss5" src="https://github.com/user-attachments/assets/1ab6dc98-1b21-49af-bf19-779835d293ad">
      </td>
      <td>
        <img width="1426" alt="ss6" src="https://github.com/user-attachments/assets/c1268b90-65b7-4155-a45f-583958ef92ad">
      </td>
    </tr>
  </table>
</div>



## Prerequisites

Ensure you have the following installed on your machine:
- Java 17+
- Maven
- Node.js 14+
- npm or yarn
- Docker (for RabbitMQ and PostgreSQL)

## Installation

### Backend Setup (Java Spring)

#### 1. Clone the repository:

        git clone https://github.com/rkmkrc/PropertyApp.git    
        cd property-app/backend

#### 2. Install dependencies and build the project:
        
        mvn clean install

### Microservices Setup

Each microservice (Listing, User, Registry, Gateway, Payment, Listing Review) should be set up individually. 

#### 1. Navigate to the microservice directory:
        
        cd property-app/backend/{microservice-name}

#### 2. Install dependencies and build the project:
        
        mvn clean install

Repeat the above steps for each microservice: listing, user, registry, gateway, payment, and listing review.

### RabbitMQ Setup

#### 1. Pull the RabbitMQ Docker image:
      
        docker pull rabbitmq:3-management

#### 2. Run RabbitMQ:
    
    
        docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

### Database Setup (PostgreSQL)        

#### 1. Pull the PostgreSQL Docker image:
    
        docker pull postgres:latest

#### 2. Run PostgreSQL:
    
    
        docker run -d --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=yourpassword -e POSTGRES_DB=yourdatabase postgres

## Configuration

### Backend Configuration

Configure the backend by editing the application.properties file located in src/main/resources/application.properties of each microservice. Update the following properties:

    spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase   
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword
    spring.rabbitmq.host=localhost
    spring.rabbitmq.port=5672
    spring.rabbitmq.username=guest
    spring.rabbitmq.password=guest

### Frontend Setup (Next.js)

#### 1. Navigate to the frontend directory:

        cd ../frontend

#### 2. Install dependencies:
        
        pnpm install

## Running the Application

### Running the Backend

Navigate to the backend directory of each microservice and start the Spring Boot application:

    cd backend/{microservice-name}
    mvn spring-boot:run

Repeat for each microservice: listing, user, registry, gateway, payment, and listing review.

### Running the Frontend

#### 1. Navigate to the frontend directory:
    
        cd frontend

#### 2. Start the Next.js application:
    
        pnpm dev

#### 3. Access the frontend application at http://localhost:3000.

## Contributing

Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the LICENSE.md file for details.
