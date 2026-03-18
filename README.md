# Personal Finance Tracker

A full-stack personal finance management application built with Spring Boot (backend REST API) and React (frontend). Track your income, expenses, monthly budgets, credit card spending, and savings goals all in one place.

## Features

- **User Authentication** - Secure signup and login with BCrypt password encoding
- **Transactions** - Track income and expense entries with category, date, and notes. Filter by type, category, or date range
- **Budget** - Set monthly budgets by category and compare against actual spending
- **Monthly Summary** - Side by side view of budgeted vs actual spending per category for any month
- **Credit Cards** - Log credit card purchases by card, category, and paid status
- **Bills** - Track recurring and one-time bills with due dates. Overdue bills highlight red, bills due within 7 days highlight yellow
- **Savings Goals** - Set savings goals with target amounts and dates. Visual progress bars show completion percentage
- **Net Worth** - Track assets and liabilities with a running net worth calculation
- **Profile** - Update personal information and password

---

## Tech Stack

**Backend**
- Java 17
- Spring Boot 3
- MySQL
- Maven
- Spring Security (BCrypt)

**Frontend**
- React 18
- React Router v6
- Axios

---

## Backend Setup

### Database Configuration

Update the following in `/backend/src/main/resources/application.properties` to match your MySQL setup:

```properties
spring.datasource.url = jdbc:mysql://localhost:3306/personalfinancetracker?createDatabaseIfNotExist=true
spring.datasource.username = root
spring.datasource.password = password
```

### Running the Backend

Import the `backend/` directory into IntelliJ or Eclipse as a Maven project.

Run `PersonalFinanceTrackerApplication.java` as a Spring Boot App.

The API will start at `http://localhost:8080`.

Tables are auto-created via Hibernate on first run (`ddl-auto = update`).

---

## Frontend Setup

Navigate to the `frontend/` directory and run:

```bash
npm install
npm start
```

The React app will start at `http://localhost:3000`.

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/signup` | Register a new user |
| POST | `/api/login` | Login and receive uId |
| GET | `/api/user/{uId}` | Get user info |
| GET | `/api/transactions/{uId}` | Get all transactions |
| POST | `/api/addTransaction/{uId}` | Add a transaction |
| PUT | `/api/updateTransaction/{uId}/{id}` | Update a transaction |
| DELETE | `/api/deleteTransaction/{uId}/{id}` | Delete a transaction |
| GET | `/api/budgets/{uId}` | Get all budgets |
| POST | `/api/addBudget/{uId}` | Add a budget |
| PUT | `/api/updateBudget/{uId}/{id}` | Update a budget |
| DELETE | `/api/deleteBudget/{uId}/{id}` | Delete a budget |
| GET | `/api/creditcards/{uId}` | Get all credit card entries |
| POST | `/api/addCreditCard/{uId}` | Add a credit card entry |
| PUT | `/api/updateCreditCard/{uId}/{id}` | Update a credit card entry |
| DELETE | `/api/deleteCreditCard/{uId}/{id}` | Delete a credit card entry |
| GET | `/api/savingsgoals/{uId}` | Get all savings goals |
| POST | `/api/addSavingsGoal/{uId}` | Add a savings goal |
| PUT | `/api/updateSavingsGoal/{uId}/{id}` | Update a savings goal |
| DELETE | `/api/deleteSavingsGoal/{uId}/{id}` | Delete a savings goal |

---

## Built Using

* Java
* Spring Boot
* MySQL
* Maven
* React
* React Router
* Axios
* CSS

## Notes
- Referenced / utlilized previous project (FitnessShift), also located on GitHub
- Used Claude AI to troubleshoot issues/errors, help with code snippets on the front end react
- All names and data in this project are completely random. They are not based off of anyone, completely random names, emails, numbers, data just to populate data to navigate the site, SQL, and Power BI Report.

## License

This project is licensed under the MIT License.
