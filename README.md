# Smart Bank Management System

A Spring Boot backend for a banking system that goes beyond basic CRUD. It solves four real-world problems: **controlling a child's spending**, **tracking recurring subscriptions**, **suggesting government schemes a customer is eligible for**, and **splitting shared bills in a group**. Everything is secured with JWT authentication.

## Problem Statement

- Banks don't let parents monitor and control spending on accounts they have set up for dependents.
- Customers lose track of recurring subscription payments and keep paying for services they no longer use.
- Many people never find out which government schemes they qualify for.
- Splitting shared expenses (trips, flats, dinners) is usually done by hand and gets messy.

## Key Features

### 1. Family / Joint Account Spending Control (core feature)
- `PARENT` and `CHILD` roles, linked through `guardianId`
- Every account has a configurable `dailyLimit`
- If a CHILD withdraws more than the limit, the transaction is saved as `PENDING_APPROVAL` and **no money is deducted**
- The linked parent can **approve** (balance deducted, status `SUCCESS`) or **reject** (status `FAILED`, no deduction)

### 2. Subscription Detector
- Register recurring subscriptions (name, amount) against an account
- The next due date is calculated automatically
- A dedicated endpoint returns the subscriptions that are due soon

### 3. Government Scheme Matcher
- A customer saves a profile once: date of birth, annual income and gender
- The app calculates age from the date of birth, then matches the profile against scheme rules stored in the database (age range, income limit, gender)
- `GET /api/schemes/suggestions` returns the eligible schemes with an official link, and a `newlyAdded` flag for schemes added in the last 30 days
- It only **suggests**. Whether to apply is up to the user, so there is no apply flow
- Schemes are plain database rows, so they can be added, edited or deleted later without code changes
- The five schemes loaded on first start are **sample data for demo purposes**. Verify real eligibility rules on the official websites before relying on them

### 4. Bill Split (Group Expenses)
- Create a group, add members by email, and record expenses paid by any member
- Each expense is split equally between all members
- The balance endpoint returns the total, each member's net balance (positive = gets money, negative = owes money), and a simple "who pays whom" list
- Only group members can view or change a group

### 5. Core Banking Operations
- Customer registration and account creation
- Deposit and withdrawal with live balance updates
- Full transaction history per account

### 6. Security
- JWT authentication (1-hour token expiry)
- Passwords hashed with BCrypt, never stored in plain text
- A custom `JwtAuthenticationFilter` validates the token on every request
- All endpoints except registration and login need a valid bearer token
- Centralized exception handling returns clean JSON errors instead of stack traces
- Profile, scheme and group APIs identify the user from the token, so a user cannot act as someone else

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3 |
| Data access | Spring Data JPA (Hibernate) |
| Database | MySQL 8 |
| Security | Spring Security + JWT (jjwt) |
| Password hashing | BCrypt |
| Build tool | Maven |

## Architecture

```
Client (Postman)
      │
      ▼
JwtAuthenticationFilter  ──►  validates the token on every request
      │
      ▼
 Controller   ──►  REST endpoints, delegates to services
      │
      ▼
  Service     ──►  business logic (limit checks, approval workflow,
      │             scheme matching, bill-split calculation)
      ▼
 Repository   ──►  Spring Data JPA interfaces
      │
      ▼
  MySQL DB
```

Packages under `org.example`: `controller`, `service`, `repository`, `entity`, `dto`, `config`, `security`, `exception`.

## API Endpoints

All endpoints need `Authorization: Bearer <token>` except the first two.

### Auth and customers
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/customers/register` | Register a new customer (no token needed) |
| POST | `/api/auth/login` | Login, returns the JWT token (no token needed) |
| GET | `/api/customers` | List all customers |
| GET | `/api/customers/{id}` | Get a customer by id |

### Accounts and transactions
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/accounts/create/{customerId}` | Create an account for a customer |
| GET | `/api/accounts` | List all accounts |
| GET | `/api/accounts/{id}` | Get an account by id |
| POST | `/api/transactions/deposit/{accountId}` | Deposit money |
| POST | `/api/transactions/withdraw/{accountId}` | Withdraw money (runs the spending-limit check) |
| PUT | `/api/transactions/approve/{transactionId}` | Approve a pending transaction |
| PUT | `/api/transactions/reject/{transactionId}` | Reject a pending transaction |
| GET | `/api/transactions` | List all transactions |
| GET | `/api/transactions/account/{accountId}` | Transaction history of an account |

### Subscriptions
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/subscriptions/add/{accountId}` | Register a subscription |
| GET | `/api/subscriptions/account/{accountId}` | List subscriptions of an account |
| GET | `/api/subscriptions/due/{accountId}` | List subscriptions that are due |

### Profile and scheme suggestions
| Method | Endpoint | Description |
|---|---|---|
| PUT | `/api/profile` | Save or update my profile (date of birth, income, gender) |
| GET | `/api/profile` | View my profile |
| GET | `/api/schemes/suggestions` | Schemes I am eligible for, based on my saved profile |

### Bill split
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/groups` | Create a group |
| GET | `/api/groups` | List the groups I belong to |
| POST | `/api/groups/{id}/members` | Add a member by email |
| POST | `/api/groups/{id}/expenses` | Add an expense paid by me |
| GET | `/api/groups/{id}/balance` | Who owes whom |

## How to Run

1. Clone the repository
2. Make sure MySQL Server is running locally
3. Update the MySQL username and password in `src/main/resources/application.properties`
4. Open the project in IntelliJ IDEA and let Maven download the dependencies
5. Run `Main.java`
6. The server starts on `http://localhost:8081`. The `smartbank` database and all tables are created automatically on first run, and the sample schemes are loaded if the schemes table is empty

## Sample Flows

### Spending control
1. Register a customer with `role: CHILD`, then log in to get a token
2. Create an account with a `dailyLimit` (for example 1000) using the token
3. Deposit funds
4. Withdraw more than the limit: status is `PENDING_APPROVAL` and the balance is unchanged
5. The parent calls the approve endpoint: status becomes `SUCCESS` and the balance is deducted

### Scheme suggestions
1. Log in, then `PUT /api/profile` with `{ "dateOfBirth": "2002-05-15", "annualIncome": 300000, "gender": "FEMALE" }`
2. `GET /api/schemes/suggestions` returns the schemes that match this profile
3. Raise `annualIncome` to 800000 and call it again: schemes with a lower income limit disappear

### Bill split
1. `POST /api/groups` with `{ "name": "Goa Trip" }`
2. `POST /api/groups/1/members` with a friend's email
3. Each person adds expenses with `POST /api/groups/1/expenses`
4. `GET /api/groups/1/balance` shows the total, each person's balance and who pays whom

## Known Limitations

- Any logged-in user can currently call `GET /api/customers` and `GET /api/accounts` and see all records. Role-based and ownership checks are planned
- Bill Split divides every expense equally between all members. Custom splits are not supported yet
- Scheme data is sample data, and scheme management (add, edit, delete) is done in the database for now

## Future Enhancements

- Admin panel with APIs to add, edit and delete schemes
- Role-based endpoint authorization and per-user data access
- Custom (unequal) splits in Bill Split
- Push notifications for pending approvals and due subscriptions
- Refresh tokens
- Swagger UI for interactive API documentation

## Author

Built as a self-learning project to understand real-world Spring Boot backend architecture: database design, JPA relationships, business logic, JWT security and MySQL integration.