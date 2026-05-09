# PROJECT REPORT

## SHOP APP MANAGEMENT SYSTEM

**Submitted in partial fulfillment of the requirements for the degree / course**
**(Insert Your Degree/Course Here)**

**Submitted By:**
(Your Name)  
(Your Roll/Registration Number)

---

## ACKNOWLEDGEMENT
I would like to express my special thanks of gratitude to my professor/guide (Insert Name) as well as the institution who gave me the golden opportunity to do this wonderful project on the "Shop App Management System", which also helped me in doing a lot of Research and I came to know about so many new things.

---

## ABSTRACT
The **Shop App Management System** is a standalone, desktop-based software application built using Java (Swing & AWT) and MySQL. It aims to completely automate the manual, day-to-day operations of a retail shop, including product inventory management, customer tracking, billing, and sales analytics. 

The system implements a secure **Role-Based Access Control (RBAC)** mechanism, distinguishing between 'Admin' and 'Staff' users. Staff members are restricted to operational tasks like adding products, viewing customers, and generating bills. In contrast, Administrators have elevated privileges to view comprehensive tabular reports and graphical sales data. Additionally, the software features automated, real-time PDF invoice generation using the iTextPDF library, allowing the shop to issue professional digital or printed receipts instantly upon checkout.

---

## 1. INTRODUCTION

### 1.1 Project Overview
In the modern retail environment, speed and accuracy are paramount. The Shop App Management System provides a comprehensive graphical user interface (GUI) that allows store owners and employees to manage inventory, process sales, and analyze business performance without needing extensive technical knowledge.

### 1.2 Purpose
The main purpose of this project is to eliminate the bottlenecks of manual ledger keeping. By centralizing data in a MySQL relational database, the system ensures data integrity, quick search capabilities, and instant mathematical calculations (e.g., GST and Grand Totals). 

### 1.3 Scope of the Project
- **Inventory Control:** Real-time stock updates upon every sale.
- **Customer CRM:** Basic Customer Relationship Management by keeping a database of buyers.
- **Point of Sale (POS):** A rapid billing interface.
- **Analytics:** Data visualization for sales trends.

---

## 2. SYSTEM ANALYSIS

### 2.1 Existing System vs Proposed System
**Existing System:**
- Manual entry of bills on paper.
- Stock taking requires physical counting of goods.
- Hard to track returning customers.
- Generating monthly sales reports takes hours or days.

**Proposed System:**
- **Automated Billing:** One-click bill calculation with auto-calculated 18% GST.
- **Real-Time Stock Deductions:** When a bill is saved, the exact quantity of products sold is instantly deducted from the database.
- **PDF Invoicing:** Immediate generation of professional invoices.
- **Instant Analytics:** Graphical bar charts provide a visual representation of the current stock versus sold inventory.

### 2.2 Hardware & Software Requirements
**Software Requirements:**
- **Operating System:** Windows 10/11, macOS, or Linux.
- **Programming Language:** Java (JDK 8 or higher).
- **Database:** MySQL Server.
- **Libraries:** `mysql-connector-j` (JDBC), `itextpdf-5.5.13.3` (PDF Generation).
- **IDE:** Visual Studio Code / Eclipse / IntelliJ IDEA.

**Hardware Requirements (Minimum):**
- **Processor:** Intel Core i3 or equivalent.
- **RAM:** 4 GB.
- **Storage:** 500 MB free space (for DB and App).

---

## 3. TECHNOLOGIES USED

### 3.1 Front-End Development (Java Swing & AWT)
The entire user interface is built natively in Java.
- **JFrame & JPanel:** For creating the main window and distinct operational panels.
- **CardLayout:** Used in the `Dashboard` to seamlessly switch between different screens (Products, Customers, Billing) without opening multiple windows.
- **Custom UI Theme:** A custom `UITheme` class applies consistent branding, colors, and typography across all screens for a premium feel.

### 3.2 Back-End Development (Java & JDBC)
- **Application Logic:** Event-driven programming via Java `ActionListeners` handles all user interactions.
- **JDBC (Java Database Connectivity):** Facilitates secure and optimized communication with the MySQL database using `PreparedStatement`s to prevent SQL Injection attacks.

### 3.3 Database (MySQL)
Used for robust relational data storage. Handles concurrent connections and complex JOIN queries required for generating sales reports.

---

## 4. SYSTEM ARCHITECTURE & DESIGN

### 4.1 Architecture
The application is built on a **Client-Server Architecture** operating locally:
1. **Presentation Layer:** The GUI (Swing Components).
2. **Business Logic Layer:** The Java controllers handling data validation, GST calculation, and PDF formatting.
3. **Data Access Layer:** The `DBConnection` class managing database transactions (Commits and Rollbacks).

### 4.2 Database Schema Design
The MySQL database is composed of interconnected tables:
1. **`users` Table:** Stores authentication data (`id`, `username`, `password`, `role`).
2. **`products` Table:** Stores inventory (`id`, `name`, `price`, `quantity`).
3. **`customers` Table:** Stores buyer demographics (`id`, `name`).
4. **`bills` Table:** Records the overarching transaction (`bill_id`, `customer_id`, `total_amount`, `gst`, `timestamp`).
5. **`bill_items` Table:** A bridge table recording exactly what was sold in each bill (`id`, `bill_id`, `product_id`, `quantity`, `price`).

---

## 5. IMPLEMENTATION & CORE FEATURES

### 5.1 Role-Based Security (`LoginFrame.java`)
The application starts at a secure login screen. Upon verifying credentials via MySQL, the system fetches the user's `role`. 
- **Staff** are directed to a limited dashboard.
- **Admins** are directed to the full dashboard with access to financial reports.

### 5.2 Dynamic Navigation (`Dashboard.java`)
A modern side-navigation bar controls the main view. Buttons are dynamically added to the sidebar based on the logged-in user's role.

### 5.3 Product & Customer Management (`ProductPanel.java`, `CustomerPanel.java`)
These modules perform complete **CRUD (Create, Read, Update, Delete)** operations. 
- A table displays all records fetched from the database in real-time.
- Staff can easily search, add new stock, or update customer details.

### 5.4 Point of Sale & Billing (`BillingPanel.java`)
The most complex module in the system:
- **Product Search:** Instantly fetches product details by ID or Name.
- **Cart Management:** Calculates item totals. If the same item is added twice, it updates the quantity rather than adding a duplicate row.
- **Stock Validation:** Throws an error if the requested quantity exceeds the available stock in the database.
- **Transactional Safety:** Uses `con.setAutoCommit(false)` to ensure that if saving a bill fails halfway through, the database rolls back, preventing data corruption.
- **PDF Generation:** Integrates iTextPDF to dynamically draw a formatted invoice containing the Shop Header, Customer Info, Itemized Table, and Grand Total, saving it locally and automatically opening it for the user to print.

### 5.5 Advanced Analytics (`SalesReportPanel.java`, `BarGraphPanel.java`)
- **Tabular Reports:** Displays a history of all bills generated.
- **Data Visualization:** Uses the Java AWT `Graphics` class (`paintComponent`) to draw custom Bar Graphs from scratch. The graphs visually compare Total Stock vs. Sold Stock, allowing administrators to visually identify fast-moving goods instantly.

---

## 6. CONCLUSION
The **Shop App Management System** successfully addresses the core needs of a modern retail store. By replacing manual paperwork with an automated, database-driven desktop application, it guarantees accurate billing, prevents stock mismatches, and secures sensitive financial data through role-based access. The inclusion of instant PDF receipts and visual data analytics elevates it from a basic data-entry tool to a complete business management solution.

---

## 7. FUTURE ENHANCEMENTS
While the current system is highly functional, future iterations could include:
1. **Barcode Scanner Integration:** To add products to the billing cart instantly.
2. **Cloud Database:** Migrating from a local MySQL database to a cloud-based AWS/GCP SQL instance for multi-branch synchronization.
3. **Email/SMS Gateway:** Automatically emailing the generated PDF invoice to the customer.
4. **Low Stock Alerts:** Automated warnings when a product's quantity falls below a defined threshold.
