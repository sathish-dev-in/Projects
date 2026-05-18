# 🛒 Zynkart — Full Stack E-Commerce Web Application

<div align="center">

![Java](https://img.shields.io/badge/Java-11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.7.18-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.6+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

**A complete, production-ready e-commerce platform built with Core Java OOP principles, Spring Boot MVC, MySQL, and JSP.**

[Features](#-features) • [Tech Stack](#-tech-stack) • [OOP Concepts](#-oop-concepts-applied) • [Setup](#-setup--run) • [Routes](#-application-routes) • [About Me](#-about-the-developer)

</div>

---

## 📌 Project Overview

**Zynkart** is a fully functional e-commerce web application built to demonstrate real-world software development skills including layered architecture, OOP design patterns, relational database design, session management, form validation, and responsive UI design.

The project covers the complete shopping lifecycle — user registration, product browsing with search and category filtering, shopping cart management, order placement with payment method selection, and a full admin panel for managing products, categories, and order fulfillment.

> **Purpose:** Built as a portfolio project to showcase 2 years of Java full-stack development experience using enterprise-grade patterns.

---

## ✨ Features

### 🧑 Customer Portal
| Feature | Description |
|---|---|
| Registration & Login | Secure BCrypt password hashing, server-side form validation |
| Product Browse | Category filter + keyword search with live results |
| Product Detail | Full description, stock status, adjustable quantity picker |
| Shopping Cart | Add / update quantity / remove items, live total calculation |
| Checkout | Shipping address entry, payment method selection (COD / UPI / Card) |
| Order History | Full order list with status badges |
| Order Cancellation | Cancel Pending/Confirmed orders with automatic stock restoration |

### 🔐 Admin Panel
| Feature | Description |
|---|---|
| Dashboard | Summary stats: total products, orders, users, pending count |
| Product Management | Add / Edit / Soft-delete products with image URL live preview |
| Category Management | Add and delete product categories |
| Order Management | Update order status across the full fulfillment pipeline |
| Recent Orders Widget | Latest 5 orders on dashboard for quick action |

---

## 🛠 Tech Stack

| Layer | Technology | Purpose |
|---|---|---|
| **Language** | Java 11 | Core application logic |
| **Framework** | Spring Boot 2.7.18 | Application container, MVC, DI |
| **ORM** | Spring Data JPA + Hibernate | Database abstraction & entity mapping |
| **Database** | MySQL 8.0 | Persistent relational data storage |
| **View Layer** | JSP + JSTL | Server-side rendered pages |
| **Frontend** | HTML5, CSS3, JavaScript (ES6) | Client-side interaction & UX |
| **UI Library** | Bootstrap 5.3 + Font Awesome 6 | Responsive design & icons |
| **Security** | Spring Security Crypto (BCrypt) | Password hashing |
| **Build Tool** | Maven (WAR packaging) | Dependency management |
| **Server** | Embedded Apache Tomcat | Application server |

---

## 🎯 OOP Concepts Applied

This project demonstrates all four OOP pillars plus advanced design patterns:

### 1. Abstraction
```java
// BaseEntity — abstract class centralises audit fields for all entities
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp private LocalDateTime createdAt;
    @UpdateTimestamp  private LocalDateTime updatedAt;
}

// Service interfaces define contracts — callers depend on abstraction, not impl
public interface ProductService {
    List<Product> findAllActive();
    Optional<Product> findById(Long id);
    Product save(ProductDTO dto);
    Product update(Long id, ProductDTO dto);
}
```

### 2. Inheritance
```java
// All 7 entities extend BaseEntity — inheriting id and audit timestamps
public class User     extends BaseEntity { ... }
public class Product  extends BaseEntity { ... }
public class Category extends BaseEntity { ... }
public class Cart     extends BaseEntity { ... }
public class CartItem extends BaseEntity { ... }
public class Order    extends BaseEntity { ... }
public class OrderItem extends BaseEntity { ... }
```

### 3. Encapsulation
```java
// All fields private; business logic encapsulated as methods on the model
public class Product extends BaseEntity {
    private BigDecimal price;
    private Integer stock;
    private boolean active;

    public boolean isAvailable()           { return active && stock > 0; }
    public boolean isLowStock()            { return stock > 0 && stock <= 5; }

    public void reduceStock(int quantity) {
        if (this.stock < quantity)
            throw new IllegalStateException("Insufficient stock");
        this.stock -= quantity;
    }
    public void increaseStock(int quantity){ this.stock += quantity; }
}
```

### 4. Polymorphism
```java
// One interface — multiple implementations, swappable via Spring DI
@Service public class ProductServiceImpl implements ProductService { ... }
@Service public class UserServiceImpl    implements UserService    { ... }
@Service public class CartServiceImpl    implements CartService    { ... }
@Service public class OrderServiceImpl   implements OrderService   { ... }
```

### Type-Safe Enums (Domain Modelling)
```java
// Order lifecycle — states enforced at compile time
public enum OrderStatus {
    PENDING("Pending"), CONFIRMED("Confirmed"), SHIPPED("Shipped"),
    DELIVERED("Delivered"), CANCELLED("Cancelled");
    private final String displayName;
}

// Role-based access
public enum Role { USER, ADMIN }
```

### Entity Relationships
```
User  1──1  Cart  1──*  CartItem  *──1  Product
User  1──*  Order 1──*  OrderItem *──1  Product
Product *──1 Category
```

---

## 🗂 Project Structure

```
zynkart/
├── src/main/
│   ├── java/com/shopease/
│   │   ├── config/
│   │   │   └── DataInitializer.java        ← Seeds sample data on startup
│   │   ├── controller/
│   │   │   ├── AuthController.java         ← Login / Register / Logout
│   │   │   ├── HomeController.java         ← Home page with latest products
│   │   │   ├── ProductController.java      ← Product listing & detail
│   │   │   ├── CartController.java         ← Cart add / update / remove
│   │   │   ├── OrderController.java        ← Checkout & order history
│   │   │   └── AdminController.java        ← Full admin panel (CRUD)
│   │   ├── dto/
│   │   │   ├── RegisterDTO.java            ← Registration form + validation
│   │   │   └── ProductDTO.java             ← Product form + validation
│   │   ├── exception/
│   │   │   ├── ResourceNotFoundException.java
│   │   │   └── GlobalExceptionHandler.java ← Centralized error handling
│   │   ├── model/                          ← JPA Entities (all extend BaseEntity)
│   │   │   ├── BaseEntity.java             ← Abstract base (OOP: Abstraction)
│   │   │   ├── User.java                   ← Role enum, isAdmin() helper
│   │   │   ├── Product.java                ← reduceStock(), isAvailable()
│   │   │   ├── Category.java
│   │   │   ├── Cart.java                   ← getTotalPrice(), getTotalItems()
│   │   │   ├── CartItem.java               ← getSubtotal()
│   │   │   ├── Order.java                  ← OrderStatus enum, isCancellable()
│   │   │   └── OrderItem.java
│   │   ├── repository/                     ← Spring Data JPA repositories
│   │   ├── service/                        ← Interfaces + Implementations
│   │   │   ├── UserService.java / impl/UserServiceImpl.java
│   │   │   ├── ProductService.java / impl/ProductServiceImpl.java
│   │   │   ├── CartService.java / impl/CartServiceImpl.java
│   │   │   ├── CategoryService.java / impl/CategoryServiceImpl.java
│   │   │   └── OrderService.java / impl/OrderServiceImpl.java
│   │   └── util/
│   │       └── SessionUtil.java            ← Session helper
│   ├── resources/
│   │   ├── application.properties
│   │   └── static/
│   │       ├── css/style.css               ← Custom responsive styles
│   │       └── js/app.js                   ← UI interactions & validation
│   └── webapp/WEB-INF/views/
│       ├── header.jsp / footer.jsp         ← Shared layout fragments
│       ├── login.jsp / register.jsp
│       ├── home.jsp                        ← Hero + categories + latest products
│       ├── products.jsp                    ← Grid with search & category filter
│       ├── product-detail.jsp              ← Detail with quantity picker
│       ├── cart.jsp                        ← Cart with live totals
│       ├── checkout.jsp                    ← Address + payment method
│       ├── orders.jsp / order-detail.jsp
│       ├── error.jsp
│       └── admin/
│           ├── dashboard.jsp               ← Stats + recent orders
│           ├── manage-products.jsp
│           ├── product-form.jsp            ← Add/edit with image preview
│           ├── manage-categories.jsp
│           └── manage-orders.jsp
├── database/
│   └── schema.sql                          ← Full DDL with foreign keys
└── pom.xml
```

---

## ⚙️ Setup & Run

### Prerequisites
- Java 11+
- Maven 3.6+
- MySQL 8.0

### Step 1 — Clone the repository
```bash
git clone https://github.com/sathish-dev-in/Projects.git
cd Projects/zynkart
```

### Step 2 — Configure database credentials
Open `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/zynkart_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 3 — Run
```bash
mvn spring-boot:run
```

### Step 4 — Open browser
```
http://localhost:8080
```

> The database schema and sample data (5 categories, 10 products, 2 users) are auto-created on first startup.

---

## 🔑 Default Login Credentials

| Role | Username | Password | Access Level |
|---|---|---|---|
| Admin | `admin` | `admin123` | Admin panel + full storefront |
| Customer | `user` | `user123` | Storefront only |

---

## 🗄 Database Schema

```sql
categories   (id, name, description, created_at, updated_at)
users        (id, username, email, password, full_name, phone, address, role, active, ...)
products     (id, name, description, price, stock, image_url, category_id, active, ...)
carts        (id, user_id)                               -- 1-to-1 with users
cart_items   (id, cart_id, product_id, quantity)         -- unique(cart_id, product_id)
orders       (id, user_id, total_amount, status, shipping_address, payment_method, ...)
order_items  (id, order_id, product_id, quantity, price)
```

Full DDL with foreign keys: [`database/schema.sql`](database/schema.sql)

---

## 🌐 Application Routes

| Route | Method | Description | Access |
|---|---|---|---|
| `/` | GET | Home page — hero + categories + latest products | Public |
| `/products` | GET | Product grid — keyword search + category filter | Public |
| `/products/{id}` | GET | Product detail page | Public |
| `/register` | GET/POST | User registration with validation | Guest |
| `/login` | GET/POST | User login | Guest |
| `/logout` | GET | Logout and session clear | Logged in |
| `/cart` | GET | View shopping cart | Logged in |
| `/cart/add` | POST | Add item to cart | Logged in |
| `/cart/update` | POST | Update item quantity | Logged in |
| `/cart/remove/{id}` | POST | Remove item from cart | Logged in |
| `/checkout` | GET | Checkout page | Logged in |
| `/order/place` | POST | Place order & reduce stock | Logged in |
| `/orders` | GET | My orders list | Logged in |
| `/orders/{id}` | GET | Order detail view | Logged in |
| `/orders/{id}/cancel` | POST | Cancel order + restore stock | Logged in |
| `/admin/dashboard` | GET | Admin stats & recent orders | Admin |
| `/admin/products` | GET/POST | Product CRUD | Admin |
| `/admin/categories` | GET/POST | Category management | Admin |
| `/admin/orders` | GET/POST | Order status updates | Admin |

---

## 👨‍💻 About the Developer

<table>
<tr>
<td>

### Sathish K
**Java Full Stack Developer**  
📧 programmer11@leaap.com  
🐙 [github.com/sathish-dev-in](https://github.com/sathish-dev-in)

</td>
</tr>
</table>

I am a passionate Java developer with **2 years of hands-on experience** building web applications using Core Java, Spring Boot, and related technologies. I enjoy solving real-world problems through clean, well-structured code and apply object-oriented design principles in every project I build.

### 💼 Technical Skills

**Backend**
- Java 11 — Core, Collections, Streams, Exception Handling
- Spring Boot — MVC, Data JPA, Security Crypto, Validation
- Hibernate ORM — Entity mappings, JPQL, transactions
- RESTful and MVC architecture patterns

**Database**
- MySQL 8 — schema design, foreign keys, indexes
- JPA relationships — OneToOne, OneToMany, ManyToOne

**Frontend**
- JSP, JSTL, HTML5, CSS3, JavaScript (ES6)
- Bootstrap 5 — responsive, mobile-first design
- Font Awesome icons

**Tools & Practices**
- Maven — dependency management and build lifecycle
- Git & GitHub — version control and collaboration
- IntelliJ IDEA / VS Code
- OOP Design Patterns — Service Layer, DTO, Repository Pattern
- BCrypt security, session management, form validation

### 🎯 What I Focus On
- Writing readable, maintainable code with clear separation of concerns
- Applying all four OOP pillars in real application design
- Building end-to-end features — from DB schema to UI
- Understanding the "why" behind every technology choice

---

## 📄 License

This project is open source under the [MIT License](../LICENSE).

---

<div align="center">

**⭐ If this project helped you learn something, please give it a star! ⭐**

*Built with Java · Spring Boot · MySQL · JSP*

</div>
