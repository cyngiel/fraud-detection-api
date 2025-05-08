# Fraud Detection API 🛡️

![Quarkus](https://img.shields.io/badge/powered%20by-Quarkus-blue)
![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Mvn](https://img.shields.io/badge/Maven-3.8+-green)
![Jwt](https://img.shields.io/badge/Auth-JWT-green)
![DB](https://img.shields.io/badge/DB-H2-purple)
![DB](https://img.shields.io/badge/Mastercard-BiN%20Lookup-orange)

## 📋 Overview

A fraud detection system built with Quarkus framework that evaluates financial transactions against configurable risk rules. The application calculates fraud risk scores based on various factors including country of origin, transaction amount, and card BIN.

## 📦 Project Structure

The application consists of multiple modules:
- `fraud-detection-core`: Main application module
- `fraud-detection-risk-score`: Risk scoring engine and rules
- `fraud-detection-common`: Shared utilities and models

## 🚀 Building and Running the Application

### Development Mode
```bash
mvn clean install
```
```bash
mvn quarkus:dev -pl fraud-detection-core
```

This starts the application in development mode with hot reloading enabled.

### Swagger UI
http://localhost:8080/q/swagger-ui/

### Database console 
http://localhost:8080/h2

no credentials required


## 🔒 Authentication

**JWT Authentication**:
   - Use pre-configured test user
   - To generate token use: /auth/login

**Test User credentials**:
   - Username: testuser
   - Password: password
```
{
  "username": "testuser",
  "password": "password"
}
```

## 🌐 API Endpoints

Based on the project structure, the application exposes the following endpoints:

| Endpoint | Method | Description | Authentication |
|----------|--------|-------------|---------------|
| `/hello` | GET | Hello from Quarkus REST | None |
| `/auth/login` | POST | Authenticate and generate JWT | None |
| `/bin-lookup` | GET | Fetch single BIN Details | JWT |
| `/risk-score` | POST | Calculate risk score | JWT |


## 🎯 Risk Scoring 
Application calculates the transaction risk score based on the BIN, transaction amount and transaction location. 

Risk Assessment Summary:

- Risk Score: Returns a value between 0 and 100.
- Risk Flag Indicator: Categorizes the risk as one of the following:
- - SAFE, WARNING, HIGH_RISK
- Detailed report outlining the specific rules that were applied and how they contributed to the final score.

###  Risk Scoring Strategy

The application employs a flexible strategy pattern for risk evaluation:

- Multiple risk scoring set rules can be configured via YAML
- Each strategy contains a set of weighted risk rules
- The system dynamically selects the appropriate strategy based on transaction context:
- Each rule contributes points to the overall risk score based on specific transaction attributes
- Rules evaluate different risk factors (country, amount, card type)

This strategy-based approach allows for fine-tuned risk assessment that adapts to different transaction scenarios while maintaining consistent evaluation logic.

### Configuration

Risk scoring is configurable through YAML configuration in the `fraud-detection-risk-score` module's resources. 

Two default rule sets are provided:

1. **DEFAULT**: Standard rule set with various risk factors:
   - High Risk Country (20 points)
   - Out of Origin Country (10 points)
   - High Transaction Amount (5 points)
   - Anonymous Prepaid Card (40 points)
   - Flex Card (30 points)

2. **WITHOUT_BIN_DETAILS**: Alternative rule set when BIN details are unavailable:
   - High Risk Country (30 points)
   - High Transaction Amount (20 points)


## 🔄 Interceptors & Request Processing

The application leverages Quarkus interceptors and filters to enhance security, traceability, and performance:

### Request Idempotency Control

An idempotency filter prevents duplicate transaction processing by tracking unique request IDs:

- The system automatically identifies repeated requests with the same ID
- Duplicate requests are blocked with a 409 Conflict response
- Each request includes a unique X-Request-ID header
- Filter generates missing request ID for each incoming request and adds it to the response headers

### Request Logging Interceptor

A robust logging mechanism captures detailed information about all API interactions:

- All inbound requests are intercepted and logged to the database
- Request method, URI, headers, and body are preserved

### 🚀 Cache Implementation

The application implements intelligent caching to optimize performance and reduce external API calls. BIN details lookups are cached, eliminating redundant requests for previously queried card numbers. This significantly reduces response times for repeated transactions with the same card BINs and minimizes load on external services. The caching mechanism automatically handles expiration of stale data, ensuring the system maintains an optimal balance between performance and data accuracy.

## 🔄 Cache Configuration

The application uses quarkus caching to optimize performance and reduce external API calls. BIN details lookups are cached, eliminating redundant requests for previously queried card numbers. 

## 🔌 External Services

The application integrates with a BIN lookup API for card information validation using
OAuth 1.0a for authenticating

## 💾 Database

The application uses an H2 in-memory database:
- URL: `jdbc:h2:~/test`
- Schema generation: `drop-and-create`
- SQL logging: disabled by default

## Logging

- Generating x-request-ID
- Logging configured with request ID tracking (Format: `%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p [%c{3.}] [X-Request-ID:%X{X-RequestID}] %s%e%n`)
- Requests info stored in the database


## 🧪 Testing

The project includes:
- JUnit tests
- Integration tests using Rest Assured
- Native tests when using the native profile
