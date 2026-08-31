# ShopMint — E-Commerce Platform

ShopMint is a simple e-commerce application built to demonstrate how real-world software can evolve from an intentionally simple implementation into a more maintainable and extensible design using Object-Oriented Principles, SOLID principles, and Design Patterns.

The project is being developed incrementally. The initial version intentionally favors straightforward code over sophisticated architecture. Once the complete basic workflow is working, individual design problems will be identified and refactored using appropriate design patterns.

---

## 1. Project Goals

The main goals of ShopMint are:

* Build a realistic e-commerce application using Java, Spring Boot, and Angular.
* Implement common e-commerce workflows.
* Start with simple, intentionally straightforward code.
* Identify problems such as tight coupling, large classes, duplicated logic, and excessive conditional statements.
* Refactor the existing implementation using appropriate design patterns.
* Demonstrate why a pattern is useful rather than simply adding patterns for the sake of it.
* Use the same application throughout the learning journey so the impact of each refactoring can be clearly demonstrated.

---

## 2. Current Status

The initial application workflow is implemented end-to-end.

### Backend

* Spring Boot application
* Java 21
* In-memory data storage
* Product management
* Customer data
* Shopping cart
* Order placement
* Order lifecycle management
* Payment processing
* Discount calculation
* Shipping calculation
* Order notifications
* REST APIs

### Frontend

* Angular application
* Product listing
* Add to Cart
* Cart display
* Cart total calculation
* Checkout options
* Place Order
* Order state display
* Order lifecycle actions
* Payment result display
* Email and SMS notification display
* Integration with Spring Boot REST APIs

The Angular application uses Angular 22 and its modern signal-based state management where required for asynchronous UI updates.

---

## 3. Repository Structure

ShopMint is maintained as a single repository containing separate backend and frontend applications.

```text
ShopMint/
│
├── apps/
│   │
│   ├── api/                 # Spring Boot backend
│   │
│   └── web/                 # Angular frontend
│
├── docs/                    # Detailed feature documentation
│
├── README.md
├── .gitignore
└── .gitattributes
```

### Backend

```text
apps/api
```

The backend exposes REST APIs and contains the e-commerce business logic.

### Frontend

```text
apps/web
```

The frontend provides a simple UI for interacting with and demonstrating the backend functionality.

---

# 4. Technology Stack

| Technology            | Purpose                                 |
| --------------------- | --------------------------------------- |
| Java 21               | Backend programming language            |
| Spring Boot           | Backend application framework           |
| Spring Web            | REST APIs                               |
| Maven                 | Backend build and dependency management |
| Angular 22            | Frontend application                    |
| TypeScript            | Frontend programming language           |
| HTML / CSS            | User interface                          |
| In-memory collections | Initial data storage                    |

### Current Scope

The application intentionally does not use a database at this stage.

Database persistence can be introduced later if required, but it is not part of the initial design-pattern learning implementation.

Automated testing is also intentionally deferred while the initial application flow is being built.

---

# 5. Functional Scope

## 5.1 Customer

The application currently works with an in-memory customer model.

A sample customer is used for demonstrating the shopping workflow.

Authentication and authorization are outside the current scope.

---

## 5.2 Product Catalog

ShopMint maintains an in-memory product catalog.

Each product contains information such as:

* Product ID
* Product name
* Description
* Category
* Price
* Available quantity

The catalog contains sample products across supported categories.

---

## 5.3 Shopping Cart

Customers can:

* View their cart
* Add products to the cart
* View cart items
* View item quantities
* View individual item totals
* View the overall cart total

The cart is currently maintained in memory.

---

## 5.4 Checkout

The checkout flow currently supports:

* Payment method selection
* Optional coupon code
* Automatic discount rule evaluation
* Shipping method selection
* Order lifecycle
* Notifications

Payment, discount, shipping, and notification processing are currently simulated and do not connect to external services.

Detailed requirements:

* [Payment](docs/payment.md)
* [Discount](docs/discount.md)
* [Notification](docs/notification.md)
* [Order-Management](docs/order-management.md)

---

# 6. Current REST API Scope

The backend currently exposes REST APIs for the main application operations.

### Products

```text
GET /products
GET /products/{id}
```

### Cart

```text
GET    /carts/{customerId}
POST   /carts/{customerId}/items
DELETE /carts/{customerId}/items/{productId}
DELETE /carts/{customerId}
```

### Orders

```text
POST /orders
GET  /orders/{id}

GET  /orders/customer/{customerId}

POST /orders/{id}/pay
POST /orders/{id}/ship
POST /orders/{id}/deliver
POST /orders/{id}/cancel
```

The API contracts may evolve as the application grows.

# 7. Application Architecture

ShopMint currently follows a simple modular-monolith style architecture.

```text
                    Angular UI
                         │
                         ▼
                  REST Controllers
                         │
                         ▼
                   Business Services
                         │
            ┌────────────┼────────────┐
            ▼            ▼            ▼
         Product        Cart         Order
                                      │
                         ┌────────────┼────────────┐
                         ▼            ▼            ▼
                      Payment      Discount     Shipping
                                      │
                                      ▼
                                Order Lifecycle
                                      │
                                      ▼
                                  Notifications
```

The initial implementation intentionally keeps the business logic straightforward.

This is important because the same code will later be used as the starting point for design-pattern refactoring.

---

# 8. Design Pattern Learning Approach

The most important objective of ShopMint is not simply to implement design patterns.

Instead, the project follows this approach:

```text
Build Working Application
          ↓
Identify Design Problems
          ↓
Understand Why the Code Is Difficult to Maintain
          ↓
Select an Appropriate Design Pattern
          ↓
Refactor Existing Code
          ↓
Compare Before vs After
```

The initial implementation intentionally contains design problems such as:

* Large service classes
* Tight coupling
* Multiple responsibilities
* Repeated conditional logic
* Difficult extensibility
* Difficult testing
* Dependencies on concrete implementations

These problems will become the motivation for introducing design patterns.

---

# 9. Future Scope

Additional capabilities may be introduced when they help demonstrate new design problems or patterns.

Possible future enhancements include:

* Product search and filtering
* Cart quantity updates
* Customer order history
* Database persistence
* Automated tests
* External payment integrations
* External notification integrations
* Authentication and authorization
* Additional business rules and integrations

Features will only be added when they contribute to the learning objectives of the project.

---

# 10. Project Philosophy

ShopMint is intentionally not designed to be a production-ready e-commerce platform.

It is a learning and demonstration project focused on understanding:

* Object-Oriented Programming
* SOLID principles
* Clean Code
* Software Design
* Design Patterns
* Refactoring
* Maintainability
* Extensibility

The application starts with simple working code and evolves gradually.

The goal is to demonstrate not only how to implement a design pattern, but more importantly:

> When should we use it, what problem does it solve, and when should we avoid it?
