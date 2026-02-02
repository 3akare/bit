# Bit - URL Shortener

A high-performance URL shortener with real-time analytics, built with Spring Boot, Redis, and MySQL.

## Features
- **Shorten URLs**: Create clean, short links instantly.
- **Custom Aliases**: Choose your own vanity URLs.
- **Real-time Analytics**: Live click tracking updates via Server-Sent Events (SSE).
- **Modern UI**: Polished, responsive interface inspired by Linktree.
- **Docker Ready**: Easy setup with Docker Compose.

## Getting Started

### Prerequisites
- Java 17+
- Docker & Docker Compose

### Running Locally

1. **Start Infrastructure (MySQL & Redis)**
   ```bash
   docker-compose up -d
   ```

2. **Run the Application**
   ```bash
   ./mvnw spring-boot:run
   ```
   The application will start on `http://localhost:8080`.

## Testing

Run the full test suite with Maven:

```bash
./mvnw test
```

## API Documentation

Once the application is running, you can explore the API endpoints via the documentation page:
- **Docs**: `http://localhost:8080/docs.html`

## License

Distributed under the MIT License. See `LICENSE.md` for more information.
