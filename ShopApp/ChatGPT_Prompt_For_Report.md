# Prompt to Copy-Paste into ChatGPT

**Instructions for you:** 
1. Copy all the text below the line.
2. Open ChatGPT.
3. Upload your friend's sample project report document to ChatGPT.
4. Paste the text below into the chat and hit send!

---

**COPY EVERYTHING BELOW THIS LINE:**

Hello ChatGPT! I am providing you with my friend's project report document as a sample. I want you to write my project report for my Java "Shop App Management System" exactly mimicking the structure, tone, and formatting of the provided sample document. 

Here are all the details, features, and technical specifications of my project that you need to use to fill in the content of the report:

### 1. Project Title & Overview
- **Title:** Shop App Management System
- **Description:** A desktop-based Java application designed to completely automate manual retail shop operations. It includes product inventory management, customer tracking, real-time billing, PDF receipt generation, and a sales analytics dashboard.

### 2. Technologies Used
- **Frontend/GUI:** Java Swing & AWT (using JFrame, JPanel, CardLayout).
- **Backend Logic:** Java (JDK 8+).
- **Database:** MySQL Server.
- **Database Connectivity:** JDBC (mysql-connector-j).
- **Additional Libraries:** iTextPDF (itextpdf-5.5.13.3) for generating PDF invoices.

### 3. Architecture & Database Schema
- **Architecture:** Monolithic Client-Server Architecture (Local Desktop Application).
- **Database Tables:**
  - `users`: id, username, password, role (admin/staff).
  - `products`: id, name, price, quantity.
  - `customers`: id, name.
  - `bills`: bill_id, customer_id, total_amount, gst, timestamp.
  - `bill_items`: id, bill_id, product_id, quantity, price.

### 4. Key Modules & Features
**1. Secure Authentication (Role-Based Access Control):**
- Users log in via a secure screen. The system fetches their role from MySQL. 
- "Staff" users get a limited dashboard (billing, products, customers).
- "Admin" users get the full dashboard including sensitive financial reports and sales graphs.

**2. Dynamic Dashboard:**
- Uses a sleek sidebar menu and `CardLayout` to switch between modules without opening multiple windows.

**3. Product & Customer Management (CRUD):**
- Full Create, Read, Update, and Delete operations for shop inventory and customer directories.
- Real-time tabular display of records.

**4. Advanced Point of Sale (Billing System):**
- Allows searching products by ID or Name.
- Automatically calculates item totals and 18% GST.
- Validates requested quantity against available MySQL database stock.
- Uses SQL Transactions (`con.setAutoCommit(false)`) to prevent database corruption if saving fails.
- Instantly deducts sold stock from the inventory table.

**5. PDF Invoice Generation:**
- Uses the `iTextPDF` library to dynamically create a formatted bill.
- The PDF includes a Shop Header, Customer info, an itemized table of purchases, GST, and the Grand Total.
- Automatically saves the file locally and opens it for printing.

**6. Analytics & Reports:**
- Dedicated "Sales Dashboard" restricted to Admins.
- Uses Java AWT Graphics to draw custom Bar Graphs from scratch.
- The graphs visually compare Total Stock vs. Sold Stock so the shop owner can track fast-moving goods instantly.

**Request:** 
Please analyze the sample document I attached, extract its exact heading structure, formatting style, and tone, and completely write my "Shop App Management System" project report using the technical details I just provided above. Make it highly professional and academic!
