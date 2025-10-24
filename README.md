# CanteenApp MVP (Java Swing Mini-Project)

This is a Java-based desktop application for a college canteen, built as a B.Tech mini-project. It uses a **Model-View-Controller (MVC)** architecture.

The application features a complete user authentication system (Student & Admin) and connects to a **MySQL (XAMPP)** database.

## Features
* Separate **Student** and **Admin** login.
* New **Student Sign Up** with email validation.
* **Database Integration:** Connects to a MySQL database to manage users, menu items, and orders.
* **Menu Display:** Students can view the live menu from the database.
* **Order System:** Students can select items, see a live-updating bill (with tax), and place an order.
* **Order Persistence:** All placed orders are saved to the database, linked to the student's account.

## Team Roles & Contributions

This project was built by a team of four, with each member responsible for a specific layer of the application.

### 1. Backend Architect (Model)
* **Name:** Ann Mary
* **Responsibility:** Built the core **OOP structure** (the "Model").
* **Files:** `src/com/canteen/model/`
* **Contribution:** Wrote all the data classes (`Student.java`, `Admin.java`, `MenuItem.java`, `Order.java`, `Bill.java`) that define the application's data.

### 2. UI Designer (View)
* **Name:** Indraja
* **Responsibility:** Built the entire **Java Swing GUI** (the "View").
* **Files:** `src/com/canteen/view/` and `src/com/canteen/main/`
* **Contribution:** Designed and created all four application windows (`LoginFrame.java`, `SignUpFrame.java`, `CanteenFrame.java`, `AdminFrame.java`) and the main starting point (`Main.java`).

### 3. Database Specialist (Persistence)
* **Name:** Adarsh
* **Responsibility:** Managed the **database schema and JDBC connection**.
* **Files:** `src/com/canteen/db/`
* **Contribution:** Set up the XAMPP (MySQL) database, created all tables (`students`, `admins`, `orders`, etc.), and wrote the complete `DatabaseManager.java` class with **JDBC** to handle all SQL queries.

### 4. Integration Lead (Controller)
* **Name:** Tippu Sahib
* **Responsibility:** Wrote all **event-handling logic** (the "Controller").
* **Files:** `src/com/canteen/controller/`
* **Contribution:** Connected all parts of the application. Wrote all `ActionListeners` to make the buttons work, linking Indraja's **View** to Adarsh's **Database** using Ann Mary's **Models**. Managed the overall application flow.
