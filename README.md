# Bit - URL Shortener

A high-performance URL shortener with real-time analytics, built with Spring Boot, Redis, and MySQL.

## Features

- **Shorten URLs** – Create clean, short links instantly
- **Custom Aliases** – Choose your own vanity URLs
- **Real-time Analytics** – Live click tracking via Server-Sent Events (SSE)
- **Modern UI** – Polished, responsive interface inspired by Linktree
- **Fully Dockerized** – One command to run everything

## Quick Start

### Prerequisites

- Docker & Docker Compose

### Run with Docker

```bash
docker-compose up --build
```

That's it! The application will be available at `http://localhost:8080`.

> **Note**: First build may take a few minutes to download dependencies.

### Stop

```bash
docker-compose down
```

To also remove the database volume:

```bash
docker-compose down -v
```

## Development (Without Docker)

If you prefer to run locally without Docker:

### Prerequisites

- Java 17+
- MySQL 8.0+ running on `localhost:3306`
- Redis running on `localhost:6379`

### Run

```bash
./mvnw spring-boot:run
```

## Testing

```bash
./mvnw test
```

## API Documentation

Once running, explore the API at:

- **Docs**: [http://localhost:8080/docs.html](http://localhost:8080/docs.html)

## License

Distributed under the MIT License. See [LICENSE.md](LICENSE.md) for more information.
