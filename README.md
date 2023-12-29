
# Task Manager Web Application

## Overview

This repository hosts a Task Management Web Application designed with a robust frontend using Angular and a powerful backend developed with Spring Boot. For data persistence, the app relies on a PostgreSQL database service that operates within a Docker container environment.

## Prerequisites

To ensure the correct functioning of this application, ensure you have the following tools and environments set up:

- Docker
- Java JDK 17
- Maven
- Node.js (v21.3.0)
- npm (v10.2.5)

## Starting the Application

### Step 1: Start the PostgreSQL Database

1. Open your terminal and navigate to the directory containing the `docker-compose.yml` file.
2. Execute the following command to start the database service:

    ```bash
    docker-compose up -d
    ```

### Step 2: Start the Backend

1. Change directories to reach the backend project folder.
2. Use Maven to build the project with:

    ```bash
    mvn clean install
    ```

3. Initiate the Spring Boot application with:

    ```bash
    java -jar target/backend-0.0.1.jar
    ```

    Note: Upon starting, the application will automatically generate a sample task for demonstration purposes.

### Step 3: Start the Frontend

1. Move to the frontend project's directory.
2. Install dependencies and launch the server by running:

    ```bash
    npm install
    ng serve
    ```

## Running Tests

### Angular Tests

To execute Angular unit tests, use the following commands:

- Execute Tests:

    ```bash
    ng test
    ```

- Generate and View Code Coverage:

    ```bash
    ng test --code-coverage
    ```

### Spring Tests

To run tests within the Spring Boot application:

- Execute Tests:

    ```bash
    mvn test
    ```

- Code Coverage:

    Utilize the coverage tools integrated into your IDE, such as IntelliJ IDEA or Eclipse, to review and assess coverage reports. You may provide a concise guide or steps particular to your project for employing your IDE’s coverage tool.

## Accessing the Application

After firing up both the backend and frontend services, access the web application via a web browser by visiting: [http://localhost:4200](http://localhost:4200)

## Screenshots

- Screenshot App:

![image](https://github.com/philipdomann/taskmanager/assets/1313090/287ac8cd-b946-435d-a46a-473d567cadd3)


- Angular Code Coverage Report:

![image](https://github.com/philipdomann/taskmanager/assets/1313090/cd7204ca-7c7b-4a17-a3f9-8c80f5aab99c)


- Spring Code Coverage:

![image](https://github.com/philipdomann/taskmanager/assets/1313090/e1fd39c7-3b4a-4d45-9f5e-f3c74359de73)


- Asana Todos Done:

![image](https://github.com/philipdomann/taskmanager/assets/1313090/aae46fd0-b780-46ad-8901-d162431942af)


- Asana Totos Possible To Do's:

![image](https://github.com/philipdomann/taskmanager/assets/1313090/8d915328-5e15-483c-9d3f-89a54d8e4d5c)
