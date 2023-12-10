# Spring Boot Portfolio Backend API

## CI/CD

[![Build Status](https://github.com/Abdelali-Elbihari/portfolio-backend-spring-boot/actions/workflows/ci-cd-fly.io.yml/badge.svg)](https://github.com/Abdelali-Elbihari/portfolio-backend-spring-boot/actions)
[![Build Status](https://github.com/Abdelali-Elbihari/portfolio-backend-spring-boot/actions/workflows/ci-cd-render.yml/badge.svg)](https://github.com/Abdelali-Elbihari/portfolio-backend-spring-boot/actions)

API endpoints and documentation are available at [Swagger UI](https://portfolio-backend-spring-boot.fly.dev/swagger-ui.html).

OpenAPI description is available in JSON format at [API-docs](https://portfolio-backend-spring-boot.fly.dev//v3/api-docs).

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
    - [Installation](#installation)
    - [Configuration](#configuration)
- [Usage](#usage)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Overview

This project serves as the backend API for my Portfolio accessible at [www.abdelalielbihari.com](www.abdelalielbihari.com). The API is built using Spring Boot, providing functionalities to manage and retrieve portfolio-related data.

## Features
- **Spring Boot 3.2.0:** Built on the Spring Boot 3.2.0 framework for robust and efficient application development.
- **MongoDB Integration:** Utilizes Spring Data MongoDB for seamless integration with MongoDB.
- **AWS S3 Integration:** Incorporates the AWS SDK for Java to interact with Amazon S3 for handling file uploads.
- **Spring Security:** Implements Spring Security 6.2.0 for securing the API.
- **JWT Authentication:** Secures user authentication with JSON Web Tokens (JWT).
- **Interactive API Documentation:** Utilizes [springdoc-openapi](https://springdoc.org/) for interactive OpenAPI 3 documentation.
- **Swagger Documentation:** Automatically generates Swagger documentation. The API is available at [Swagger UI](https://portfolio-backend-spring-boot.fly.dev/swagger-ui.html).
- **Testcontainers:** Leverages Testcontainers for testing MongoDB interactions within a containerized environment.
- **MapStruct:** Implements MapStruct for simplified bean mappings.

## Prerequisites

Ensure you have the following installed before setting up the project:

- Java Development Kit (JDK) 17
- MongoDB (for local development and testing)
- Amazon S3 credentials (for AWS S3 integration)

## Getting Started

### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/Abdelali-Elbihari/portfolio-backend-spring-boot.git
    cd portfolio-backend-spring-boot
    ```

2. Build the project:

    ```bash
    ./mvnw clean install
    ```

### Configuration

1. Configure MongoDB:
    - Ensure MongoDB is running locally or update `application.properties` with the appropriate connection details.

2. Configure AWS S3:
    - Set your AWS S3 credentials and configuration in `application.properties`.

3. Run the application:

    ```bash
    ./mvnw spring-boot:run
    ```

## Usage

The API endpoints and documentation are available at [Swagger UI](https://portfolio-backend-spring-boot.fly.dev/swagger-ui.html).

The OpenAPI 3 description is available in JSON format at [API-docs](https://portfolio-backend-spring-boot.fly.dev//v3/api-docs).

## Testing

The project includes unit tests and integration tests. Run tests using:

```bash
./mvnw test
