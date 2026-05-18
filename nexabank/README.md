# NexaBank — Digital Banking Microservices Platform

<div align="center">

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2023.0-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-4479A1?style=for-the-badge&logo=postgresql&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-7.0-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-17-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![Tailwind](https://img.shields.io/badge/Tailwind_CSS-3.4-38B2AC?style=for-the-badge&logo=tailwindcss&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)

**A production-quality digital banking platform built with microservices architecture, showcasing advanced Java 17 features, Spring Cloud, and a modern Angular 17 frontend.**

</div>

---

## Architecture

```
                           ┌─────────────────────────────────────────────────────┐
                           │             SERVICE DISCOVERY (Eureka)               │
                           │                   Port: 8761                         │
                           └─────────────────────┬───────────────────────────────┘
                                                 │ registers
         ┌───────────────────────────────────────┼───────────────────────────────────────┐
         │                                       │                                       │
┌────────┴────────┐  JWT        ┌────────────────┴──────┐           ┌────────────────────┐
│  Angular 17     │  requests   │    API GATEWAY         │           │   DATABASES         │
│  Frontend       ├────────────►│    Port: 8080          │           │                     │
│  Port: 4200     │             │  JWT Auth Filter        │           │  PostgreSQL (5432)  │
│  Tailwind CSS   │             │  Load Balancing (lb://) │           │  ├─ nexabank_auth   │
└─────────────────┘             └────────┬───────────────┘           │  ├─ nexabank_accts  │
                                         │ routes to                 │  └─ nexabank_txns   │
              ┌──────────────────┬───────┴──────┬────────────────┐   │                     │
              │                  │              │                │   │  MongoDB (27017)    │
    ┌─────────┴──────┐  ┌────────┴───┐  ┌──────┴─────┐  ┌──────┴─┐ │  └─ nexabank_notifs │
    │  AUTH SERVICE   │  │  ACCOUNT   │  │TRANSACTION │  │NOTIFIC.│ └────────────────────┘
    │  Port: 8081     │  │  SERVICE   │  │  SERVICE   │  │SERVICE │
    │  PostgreSQL     │  │  Port:8082 │  │  Port:8083 │  │Pt:8084 │
    │  JWT issuance   │  │  PostgreSQL│  │  Feign     │  │MongoDB │
    └─────────────────┘  └────────────┘  └────────────┘  └────────┘
```

---

## Services

| Service | Port | Database | Purpose |
|---|---|---|---|
| **discovery-server** | 8761 | — | Eureka service registry for microservice discovery |
| **api-gateway** | 8080 | — | Routes requests, validates JWT, enriches headers |
| **auth-service** | 8081 | PostgreSQL (nexabank_auth) | User registration, login, JWT issuance |
| **account-service** | 8082 | PostgreSQL (nexabank_accounts) | Account CRUD, deposits, debit/credit |
| **transaction-service** | 8083 | PostgreSQL (nexabank_transactions) | Fund transfers, transaction history |
| **notification-service** | 8084 | MongoDB (nexabank_notifications) | Async notifications via CompletableFuture |
| **frontend** | 4200 | — | Angular 17 + Tailwind CSS banking UI |

---

## Advanced Java 17 Concepts Demonstrated

| Concept | Where Used | Key File |
|---|---|---|
| **Records** | DTOs: `TransferRequest`, `AuthResponse`, `AccountResponse`, `ApiResponse<T>` | `dto/*.java` in all services |
| **Sealed Classes** | `AppException` hierarchy with exhaustive `permits` | `exception/AppException.java` |
| **Pattern Matching (switch)** | `GlobalExceptionHandler` — exhaustive switch on sealed subclasses | `exception/GlobalExceptionHandler.java` |
| **Switch Expressions** | `AccountType.getInterestRate()`, `TransactionType.getDescription()` | `enums/AccountType.java`, `enums/TransactionType.java` |
| **Generics** | `ApiResponse<T>` record, `BaseService<T, ID>` interface | `dto/ApiResponse.java`, `service/BaseService.java` |
| **CompletableFuture** | Async MongoDB notification writes with thread pool | `service/impl/NotificationServiceImpl.java` |
| **Custom Annotations** | `@AuditLog(action="FUND_TRANSFER")` on service methods | `annotation/AuditLog.java` |
| **AOP** | `AuditLogAspect` intercepts `@AuditLog` — logs timing, action, result | `aspect/AuditLogAspect.java` |
| **Functional Interfaces** | `NotificationHandler<T>` with `andThen()` default, `TokenValidator`, `TransactionProcessor` | `functional/*.java` |
| **Text Blocks** | Multi-line JPQL queries in repositories, seed log messages | `repository/*.java`, `config/DataInitializer.java` |
| **Optional** | `userRepository.findByEmail().orElseThrow()`, `accountRepository.findById().map()` | `service/impl/*.java` |

---

## Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.2.5 |
| **Service Discovery** | Spring Cloud Netflix Eureka 2023.0.1 |
| **API Gateway** | Spring Cloud Gateway (WebFlux) |
| **Inter-service Calls** | Spring Cloud OpenFeign |
| **Authentication** | Spring Security + JJWT 0.12.5 (HS256) |
| **Relational DB** | PostgreSQL 15 with Spring Data JPA / Hibernate |
| **NoSQL DB** | MongoDB 7.0 with Spring Data MongoDB |
| **Async Processing** | `@Async` + `CompletableFuture` + `ThreadPoolTaskExecutor` |
| **AOP** | Spring AOP + AspectJ (`@Around`) |
| **Frontend Framework** | Angular 17 (Standalone, no NgModule) |
| **Styling** | Tailwind CSS 3.4 + @tailwindcss/forms |
| **Build** | Maven (per service), Angular CLI |
| **Containerization** | Docker + Docker Compose |

---

## Setup and Running

### Option 1: Docker Compose (Recommended)

```bash
# Clone and enter project
cd nexabank

# Start all infrastructure + services
docker-compose up -d

# View logs
docker-compose logs -f auth-service

# Stop everything
docker-compose down
```

### Option 2: Local Development

#### Prerequisites
- Java 17
- Maven 3.8+
- PostgreSQL 15 (port 5432)
- MongoDB 7.0 (port 27017)
- Node.js 18+ (for frontend)
- Angular CLI 17

#### 1. Set up databases
```sql
CREATE DATABASE nexabank_auth;
CREATE DATABASE nexabank_accounts;
CREATE DATABASE nexabank_transactions;
```

#### 2. Start services in order
```bash
# Terminal 1: Discovery Server
cd discovery-server && mvn spring-boot:run

# Terminal 2: API Gateway
cd api-gateway && mvn spring-boot:run

# Terminal 3: Auth Service
cd auth-service && mvn spring-boot:run

# Terminal 4: Account Service
cd account-service && mvn spring-boot:run

# Terminal 5: Transaction Service
cd transaction-service && mvn spring-boot:run

# Terminal 6: Notification Service
cd notification-service && mvn spring-boot:run

# Terminal 7: Frontend
cd frontend && npm install && ng serve
```

#### 3. Access the application
- **Frontend**: http://localhost:4200
- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080

---

## API Endpoints

### Auth Service (`/api/auth`)
| Method | Path | Auth | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | No | Register new user |
| `POST` | `/api/auth/login` | No | Login and get JWT |
| `GET` | `/api/auth/validate` | Bearer | Validate token |

### Account Service (`/api/accounts`)
| Method | Path | Auth | Description |
|---|---|---|---|
| `POST` | `/api/accounts` | Bearer | Create new account |
| `GET` | `/api/accounts` | Bearer | Get my accounts |
| `GET` | `/api/accounts/{id}` | Bearer | Get account by ID |
| `POST` | `/api/accounts/{id}/deposit` | Bearer | Deposit funds |

### Transaction Service (`/api/transactions`)
| Method | Path | Auth | Description |
|---|---|---|---|
| `POST` | `/api/transactions/transfer` | Bearer | Fund transfer |
| `GET` | `/api/transactions/account/{id}` | Bearer | Transaction history |
| `GET` | `/api/transactions/{id}` | Bearer | Get transaction |

### Notification Service (`/api/notifications`)
| Method | Path | Auth | Description |
|---|---|---|---|
| `GET` | `/api/notifications` | Bearer | All notifications |
| `GET` | `/api/notifications/unread` | Bearer | Unread notifications |
| `GET` | `/api/notifications/unread-count` | Bearer | Unread count |
| `PATCH` | `/api/notifications/{id}/read` | Bearer | Mark as read |

---

## Default Credentials

| Role | Email | Password |
|---|---|---|
| Admin | admin@nexabank.com | admin123 |
| Customer | john.doe@nexabank.com | user123 |

> Credentials are seeded automatically on first startup via `DataInitializer`.

---

## Example API Usage

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john.doe@nexabank.com","password":"user123"}'
```

### Create Account (with Bearer token)
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"accountType":"SAVINGS","initialDeposit":1000.00}'
```

### Transfer Funds
```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"fromAccountId":1,"toAccountId":2,"amount":250.00,"description":"Test transfer"}'
```

---

## Project Highlights

### Microservices Architecture
- **Service Discovery**: Eureka server for dynamic service registration
- **API Gateway**: Centralized JWT validation, header enrichment (`X-User-Id`, `X-User-Role`), CORS
- **Load Balancing**: `lb://` prefix enables client-side load balancing via Eureka
- **Feign Clients**: Declarative HTTP clients between transaction-service and account-service

### Security
- JWT tokens signed with HMAC-SHA256 (JJWT 0.12.x API)
- Token validated at API Gateway — backend services trust gateway headers
- BCrypt password hashing
- Stateless sessions

### Data Architecture
- **PostgreSQL**: Relational data (users, accounts, transactions) with JPA/Hibernate
- **MongoDB**: Flexible document storage for notifications
- **Separate databases per service**: True data isolation following microservices principles

---

## Developer

**Sathish K** — Java Full Stack Developer

GitHub: [github.com/sathish-dev-in](https://github.com/sathish-dev-in)
