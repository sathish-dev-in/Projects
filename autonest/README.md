# 🔧 AutoNest — Garage Management System

<div align="center">

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-7.0-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-17-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![TailwindCSS](https://img.shields.io/badge/Tailwind_CSS-3.x-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

**A production-quality, full-stack garage management system built with modern Java 17 features, dual-database architecture (PostgreSQL + MongoDB), and a responsive Angular 17 frontend.**

</div>

---

## 📋 Project Overview

AutoNest is a complete **Garage Management System** that allows an automotive service center to manage:
- Customer records and vehicle registrations
- Service orders from creation through completion
- Parts/inventory with low-stock alerts
- Auto-generated invoices on service completion
- Rich service reports stored in MongoDB
- Asynchronous audit logging for every mutation

The project is designed to showcase advanced Java 17 concepts, Spring Boot 3.2 enterprise patterns, and Angular 17 standalone component architecture.

---

## ⚡ Advanced Java Concepts Used

| # | Concept | Where Used | Code Snippet |
|---|---------|------------|--------------|
| 1 | **Java Records** | All request DTOs | `public record CustomerRequest(@NotBlank String firstName, ...)` |
| 2 | **Sealed Classes** | Exception hierarchy | `public sealed class AppException permits ResourceNotFoundException, BusinessException` |
| 3 | **Generics** | `GenericResponse<T>`, `BaseService<T,R,ID>` | `public record GenericResponse<T>(boolean success, String message, T data, ...)` |
| 4 | **Custom Functional Interface** | `EntityMapper<E,D>` with `andThen()` | `@FunctionalInterface interface EntityMapper<E,D> { D map(E entity); default <R> EntityMapper<E,R> andThen(...) }` |
| 5 | **Streams + Lambdas + Method References** | All service layer | `.stream().filter(Customer::isActive).toList()` |
| 6 | **Optional** | Null-safe service returns | `customerRepository.findById(id).orElseThrow(...)` |
| 7 | **CompletableFuture** | Async MongoDB logging | `CompletableFuture.runAsync(() -> activityLogRepository.save(log))` |
| 8 | **Enum with abstract method** | `ServiceStatus` | `PENDING { public boolean canTransitionTo(ServiceStatus t) { return Set.of(IN_PROGRESS, CANCELLED).contains(t); } }` |
| 9 | **Custom Annotation + AOP** | `@Loggable` + `LoggingAspect` | `@Around("@annotation(com.autonest.annotation.Loggable)")` |
| 10 | **Default interface methods** | `BaseService` | `default String generateMessage(String entity, String action) { return entity + " " + action + " successfully"; }` |
| 11 | **Pattern Matching instanceof** | `GlobalExceptionHandler` | `case ResourceNotFoundException rne -> "NOT_FOUND: " + rne.getMessage()` |
| 12 | **Switch Expressions** | Status transitions, stock ops | `String css = switch (this) { case PENDING -> "badge-yellow"; ... }` |
| 13 | **Builder Pattern** | All entities via Lombok | `@SuperBuilder` on `BaseEntity`, `@Builder` on all entities |
| 14 | **@Validated + Bean Validation** | All controllers | `@Validated` on controller class + `@Valid` on request body |

---

## ✨ Features

### Customer Management
- Full CRUD operations for customers
- Search customers by name
- Soft-delete (preserves history)
- Unique email validation

### Vehicle Management
- Register vehicles linked to customers
- Support for Sedan, SUV, Hatchback, Truck, Van, Motorcycle, Electric, Hybrid
- License plate uniqueness enforcement
- VIN tracking

### Service Orders
- Create service orders with vehicle, mechanic, service type, and cost estimate
- Full lifecycle state machine: `PENDING → IN_PROGRESS → COMPLETED / CANCELLED`
- `ServiceStatus` enum enforces valid transitions via `canTransitionTo()` abstract method
- Auto-invoice generation when a service is completed
- Status-aware rich service reports written to MongoDB

### Parts & Inventory
- Track parts with part numbers, pricing, stock quantities
- Low-stock alerts when quantity falls below minimum level
- Stock adjustment operations: ADD, SUBTRACT, SET
- Categorized parts with supplier tracking

### Dashboard
- Aggregate statistics: customers, vehicles, active/pending/completed services, revenue
- Low-stock part count at a glance
- Active service order table with inline status updates

### Audit Trail & Reports
- Every create/update/delete writes an `ActivityLog` document to MongoDB **asynchronously** via `CompletableFuture.runAsync()`
- Service completion generates a rich `ServiceReport` MongoDB document including parts used, work performed, and cost

---

## 🛠 Tech Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Language | Java | 17 |
| Backend Framework | Spring Boot | 3.2.5 |
| Primary Database | PostgreSQL | 15+ |
| Secondary Database | MongoDB | 7.0+ |
| ORM | Spring Data JPA / Hibernate | 6.x |
| Document Store | Spring Data MongoDB | 4.x |
| Validation | Jakarta Validation / Hibernate Validator | 3.x |
| AOP | Spring AOP + AspectJ | 6.x |
| Build Tool | Maven | 3.9+ |
| Code Generation | Lombok | 1.18.x |
| Frontend Framework | Angular | 17 |
| CSS Framework | Tailwind CSS | 3.x |
| HTTP Client | Angular HttpClient | 17 |
| Frontend Build | Angular CLI + esbuild | 17 |

---

## 📁 Project Structure

```
autonest/
├── backend/                          # Spring Boot 3.2 Application
│   ├── pom.xml
│   └── src/main/java/com/autonest/
│       ├── AutoNestApplication.java
│       ├── annotation/
│       │   └── Loggable.java              # Custom logging annotation
│       ├── aspect/
│       │   └── LoggingAspect.java         # AOP around advice
│       ├── config/
│       │   ├── CorsConfig.java            # CORS for Angular
│       │   ├── JpaConfig.java             # JPA auditing config
│       │   └── DataInitializer.java       # Seed data on startup
│       ├── controller/
│       │   ├── CustomerController.java
│       │   ├── VehicleController.java
│       │   ├── ServiceOrderController.java
│       │   ├── PartController.java
│       │   └── DashboardController.java
│       ├── dto/
│       │   ├── request/                   # Java Records (immutable DTOs)
│       │   │   ├── CustomerRequest.java
│       │   │   ├── VehicleRequest.java
│       │   │   ├── ServiceOrderRequest.java
│       │   │   └── PartRequest.java
│       │   └── response/
│       │       ├── GenericResponse.java   # Generic<T> record wrapper
│       │       └── DashboardStatsDTO.java
│       ├── entity/
│       │   ├── postgres/                  # JPA entities
│       │   │   ├── BaseEntity.java
│       │   │   ├── Customer.java
│       │   │   ├── Vehicle.java
│       │   │   ├── Mechanic.java
│       │   │   ├── ServiceOrder.java
│       │   │   ├── Part.java
│       │   │   └── Invoice.java
│       │   └── mongo/                     # MongoDB documents
│       │       ├── ServiceReport.java
│       │       └── ActivityLog.java
│       ├── enums/
│       │   ├── ServiceStatus.java         # Enum with abstract canTransitionTo()
│       │   ├── VehicleType.java
│       │   └── ServiceType.java
│       ├── exception/
│       │   ├── AppException.java          # Sealed class
│       │   ├── ResourceNotFoundException.java
│       │   ├── BusinessException.java
│       │   └── GlobalExceptionHandler.java
│       ├── functional/
│       │   └── EntityMapper.java          # Custom functional interface
│       ├── repository/
│       │   ├── postgres/                  # JPA repositories
│       │   └── mongo/                     # MongoDB repositories
│       └── service/
│           ├── BaseService.java           # Generic interface + default methods
│           ├── CustomerService.java
│           ├── VehicleService.java
│           ├── ServiceOrderService.java
│           ├── PartService.java
│           ├── DashboardService.java
│           └── impl/                      # All implementations
│
└── frontend/                         # Angular 17 Standalone App
    ├── package.json
    ├── tailwind.config.js
    └── src/
        ├── main.ts
        ├── styles.css                     # Tailwind directives + custom classes
        └── app/
            ├── app.component.ts/html      # Root layout (sidebar + router-outlet)
            ├── app.config.ts              # provideRouter, provideHttpClient
            ├── app.routes.ts              # Lazy-loaded feature routes
            ├── core/
            │   ├── models/index.ts        # All TypeScript interfaces
            │   └── services/             # HTTP services per domain
            ├── shared/
            │   ├── sidebar/              # Dark sidebar navigation
            │   └── navbar/               # Top navbar with toggle
            └── features/
                ├── dashboard/            # Stat cards + active orders table
                ├── customers/            # List + slide-over form
                ├── vehicles/             # List + form with customer select
                ├── service-orders/       # List + status tabs + inline transitions
                └── parts/               # Inventory table + stock adjust modal
```

---

## 🚀 Setup & Run

### Prerequisites

| Tool | Version |
|------|---------|
| Java | 17+ |
| Maven | 3.9+ |
| Node.js | 20+ |
| npm | 10+ |
| PostgreSQL | 15+ |
| MongoDB | 7.0+ |

---

### Backend Setup

**1. Create the PostgreSQL database:**
```sql
CREATE DATABASE autonest_db;
```

**2. Ensure MongoDB is running** (default port 27017):
```bash
mongod --dbpath /data/db
```

**3. Update credentials** (if different from defaults) in `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/autonest_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.data.mongodb.uri=mongodb://localhost:27017/autonest_logs
```

**4. Build and run the backend:**
```bash
cd autonest/backend
mvn clean install
mvn spring-boot:run
```

The API will start at **http://localhost:8080**

The `DataInitializer` seeds the database on first run with:
- 3 Mechanics (Carlos Rivera, Diana Chen, James Patel)
- 5 Customers (Arjun Sharma, Priya Nair, Rahul Verma, Sneha Iyer, Vikram Singh)
- 5 Vehicles (Honda City, Hyundai Creta, Maruti Swift, Tata Nexon, Toyota Fortuner)
- 3 Service Orders (OIL_CHANGE completed, AC_SERVICE in progress, BRAKE_SERVICE pending)
- 10 Parts (Engine Oil Filter, Air Filter, Brake Pads, Spark Plugs, etc.)

---

### Frontend Setup

```bash
cd autonest/frontend
npm install
npm start
```

The Angular app will start at **http://localhost:4200**

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/dashboard/stats` | Dashboard aggregate statistics |
| `GET` | `/api/customers` | List all active customers |
| `POST` | `/api/customers` | Create a new customer |
| `GET` | `/api/customers/{id}` | Get customer by ID |
| `PUT` | `/api/customers/{id}` | Update customer |
| `DELETE` | `/api/customers/{id}` | Soft-delete customer |
| `GET` | `/api/customers/search?name=` | Search customers by name |
| `GET` | `/api/vehicles` | List all active vehicles |
| `POST` | `/api/vehicles` | Register a new vehicle |
| `GET` | `/api/vehicles/{id}` | Get vehicle by ID |
| `PUT` | `/api/vehicles/{id}` | Update vehicle |
| `DELETE` | `/api/vehicles/{id}` | Soft-delete vehicle |
| `GET` | `/api/vehicles/customer/{customerId}` | Get vehicles for a customer |
| `GET` | `/api/service-orders` | List all service orders |
| `POST` | `/api/service-orders` | Create a service order |
| `GET` | `/api/service-orders/{id}` | Get service order by ID |
| `PUT` | `/api/service-orders/{id}` | Update service order |
| `DELETE` | `/api/service-orders/{id}` | Delete service order |
| `PATCH` | `/api/service-orders/{id}/status?status=` | Update order status |
| `GET` | `/api/service-orders/active` | Get active (pending/in-progress) orders |
| `GET` | `/api/service-orders/status/{status}` | Filter orders by status |
| `GET` | `/api/parts` | List all parts |
| `POST` | `/api/parts` | Add a new part |
| `GET` | `/api/parts/{id}` | Get part by ID |
| `PUT` | `/api/parts/{id}` | Update part details |
| `DELETE` | `/api/parts/{id}` | Soft-delete part |
| `GET` | `/api/parts/low-stock` | Get parts below minimum stock level |
| `PATCH` | `/api/parts/{id}/stock?quantity=&operation=` | Adjust stock (ADD/SUBTRACT/SET) |

All responses are wrapped in `GenericResponse<T>`:
```json
{
  "success": true,
  "message": "Customer created successfully",
  "data": { ... },
  "timestamp": "2024-01-15T10:30:00"
}
```

---

## 🖥 Frontend Screenshots

| Screen | Description |
|--------|-------------|
| Dashboard | Stat cards for customers, vehicles, active services, revenue + active orders table |
| Customers | Searchable table with initials avatar + slide-over create/edit form |
| Vehicles | Type-icon table showing owner, license plate, VIN + form with customer dropdown |
| Service Orders | Status-filtered tabs, inline status transition buttons, order details |
| Parts | Inventory table with low-stock highlighting, stock adjust modal |

---

## 👨‍💻 Developer

**Sathish K** — Java Full Stack Developer

Passionate about building production-quality Java applications with modern frameworks and best practices.

- **GitHub:** [github.com/sathish-dev-in](https://github.com/sathish-dev-in)

---

<div align="center">

*Built with Spring Boot 3.2 + Java 17 + Angular 17 + PostgreSQL + MongoDB*

</div>
