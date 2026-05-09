import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.File;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;

public class BillingPanel extends JPanel {
    
    private JTextField customerField, searchField, quantityField;
    private JTable billingTable;
    private DefaultTableModel billingModel;
    private JLabel totalLabel, grandTotalLabel;
    private JButton addButton, removeButton, generateBillButton, clearButton, printButton;
    
    private double totalAmount = 0.0;
    private double grandTotal = 0.0;

    public BillingPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        // Customer input field
        customerField = new JTextField(20);
        customerField.setFont(new Font("Arial", Font.PLAIN, 14));
        customerField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        // Search field for products
        searchField = new JTextField(15);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Quantity field
        quantityField = new JTextField(5);
        quantityField.setFont(new Font("Arial", Font.PLAIN, 14));
        quantityField.setText("1");
        quantityField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        // Billing table
        String[] columns = {"Product ID", "Name", "Price", "Qty", "Total"};
        billingModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 || columnIndex == 4 ? Double.class : Object.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only quantity is editable
            }
        };
        
        billingTable = new JTable(billingModel);
        billingTable.setRowHeight(25);
        billingTable.setFont(new Font("Arial", Font.PLAIN, 12));
        billingTable.setSelectionBackground(Color.WHITE);
        billingTable.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = billingTable.getTableHeader();
        header.setUI(new javax.swing.plaf.basic.BasicTableHeaderUI());
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        
        // Labels
        totalLabel = new JLabel("Total: ₹0.00", JLabel.RIGHT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        grandTotalLabel = new JLabel("Grand Total: ₹0.00", JLabel.RIGHT);
        grandTotalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        grandTotalLabel.setForeground(new Color(46, 204, 113)); // Green color
        
        // Buttons
        addButton = createButton("Add Product", UITheme.SUCCESS_COLOR);
        removeButton = createButton("Remove", UITheme.DANGER_COLOR);
        generateBillButton = createButton("Save Bill", UITheme.PRIMARY_COLOR);
        clearButton = createButton("Clear Bill", UITheme.NEUTRAL_COLOR);
        printButton = createButton("Print PDF", new Color(155, 89, 182)); // Purple print button
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }

    private void setupLayout() {
        // Top panel - Customer input and product search
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Customer Name:"));
        topPanel.add(customerField);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(new JLabel("Search Product:"));
        topPanel.add(searchField);
        topPanel.add(new JLabel("Qty:"));
        topPanel.add(quantityField);
        topPanel.add(addButton);
        topPanel.add(removeButton);
        
        // Center panel - Billing table
        JScrollPane scrollPane = new JScrollPane(billingTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        
        // Bottom panel - Summary and actions
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Bill Summary"));
        summaryPanel.add(totalLabel);
        summaryPanel.add(grandTotalLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(clearButton);
        buttonPanel.add(generateBillButton);
        buttonPanel.add(printButton);
        
        bottomPanel.add(summaryPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        // Search product on Enter key
        searchField.addActionListener(e -> searchProduct());
        
        // Add button
        addButton.addActionListener(e -> searchProduct());
        
        // Remove button
        removeButton.addActionListener(e -> removeSelectedRow());
        
        // Clear button
        clearButton.addActionListener(e -> clearBill());
        
        // Save bill button
        generateBillButton.addActionListener(e -> saveBill());
        
        // Print PDF button
        printButton.addActionListener(e -> generatePDF());
        
        // Table model listener for quantity changes
        billingModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                updateTotals();
            }
        });
    }
    private int getOrCreateCustomer(Connection con, String customerName) throws SQLException {
        // First try to find existing customer
        PreparedStatement psFind = con.prepareStatement(
            "SELECT id FROM customers WHERE name = ?"
        );
        psFind.setString(1, customerName);
        ResultSet rs = psFind.executeQuery();
        
        if (rs.next()) {
            int customerId = rs.getInt("id");
            rs.close();
            psFind.close();
            return customerId;
        }
        rs.close();
        psFind.close();
        
        // If not found, create new customer
        PreparedStatement psInsert = con.prepareStatement(
            "INSERT INTO customers (name) VALUES (?)",
            Statement.RETURN_GENERATED_KEYS
        );
        psInsert.setString(1, customerName);
        psInsert.executeUpdate();
        
        ResultSet generatedKeys = psInsert.getGeneratedKeys();
        int newCustomerId = -1;
        if (generatedKeys.next()) {
            newCustomerId = generatedKeys.getInt(1);
        }
        
        generatedKeys.close();
        psInsert.close();
        
        return newCustomerId;
    }

    private void searchProduct() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) return;
        
        try {
            Connection con = DBConnection.getConnection();
            
            // Search by ID or name
            String query;
            boolean isNumeric = searchTerm.matches("\\d+");
            
            if (isNumeric) {
                query = "SELECT * FROM products WHERE id = ?";
            } else {
                query = "SELECT * FROM products WHERE name LIKE ?";
            }
            
            PreparedStatement ps = con.prepareStatement(query);
            if (isNumeric) {
                ps.setString(1, searchTerm);
            } else {
                ps.setString(1, "%" + searchTerm + "%");
            }
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                addProductToBill(rs);
                searchField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addProductToBill(ResultSet rs) throws SQLException {
        String productId = rs.getString("id");
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        
        // Get quantity from field
        int quantity = 1;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check stock availability
        int availableStock = rs.getInt("quantity");
        if (quantity > availableStock) {
            JOptionPane.showMessageDialog(this, "Only " + availableStock + " items available in stock!", "Stock Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Calculate total
        double total = price * quantity;
        
        // Check if product already exists in bill
        for (int i = 0; i < billingModel.getRowCount(); i++) {
            if (billingModel.getValueAt(i, 0).toString().equals(productId)) {
                // Update quantity
                int existingQty = (Integer) billingModel.getValueAt(i, 3);
                int newTotalQty = existingQty + quantity;
                
                // Check stock again for combined quantity
                if (newTotalQty > availableStock) {
                    JOptionPane.showMessageDialog(this, "Only " + availableStock + " items available in stock! You already have " + existingQty + " in bill.", "Stock Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                billingModel.setValueAt(newTotalQty, i, 3);
                updateRowTotal(i);
                return;
            }
        }
        
        // Add new row
        billingModel.addRow(new Object[]{
            productId, name, price, quantity, total
        });
        
        updateTotals();
    }

    private double calculateGST() {
        // 18% GST on grand total
        return grandTotal * 0.18;
    }

    private void updateRowTotal(int row) {
        double price = (Double) billingModel.getValueAt(row, 2);
        int quantity = (Integer) billingModel.getValueAt(row, 3);
        double total = price * quantity;
        
        billingModel.setValueAt(total, row, 4);
    }

    private void updateTotals() {
        totalAmount = 0.0;
        
        for (int i = 0; i < billingModel.getRowCount(); i++) {
            double price = (Double) billingModel.getValueAt(i, 2);
            int quantity = (Integer) billingModel.getValueAt(i, 3);
            
            totalAmount += price * quantity;
        }
        
        grandTotal = totalAmount;
        
        totalLabel.setText("Total: ₹" + String.format("%.2f", totalAmount));
        grandTotalLabel.setText("Grand Total: ₹" + String.format("%.2f", grandTotal));
    }

    private void removeSelectedRow() {
        int selectedRow = billingTable.getSelectedRow();
        if (selectedRow != -1) {
            billingModel.removeRow(selectedRow);
            updateTotals();
        }
    }

    private void clearBill() {
        billingModel.setRowCount(0);
        updateTotals();
        customerField.setText("");
        searchField.setText("");
        quantityField.setText("1");
    }

    private void saveBill() {
        if (billingModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please add products to the bill!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String customerName = customerField.getText().trim();
        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Connection con = DBConnection.getConnection();
            con.setAutoCommit(false);
            
            // Insert or get customer
            int customerId = getOrCreateCustomer(con, customerName);
            
            // Insert bill record
            PreparedStatement psBill = con.prepareStatement(
                "INSERT INTO bills (customer_id, total_amount, gst) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            psBill.setInt(1, customerId);
            psBill.setDouble(2, grandTotal);
            psBill.setDouble(3, calculateGST());
            psBill.executeUpdate();
            
            // Get bill ID
            ResultSet rsKeys = psBill.getGeneratedKeys();
            int billId = 0;
            if (rsKeys.next()) {
                billId = rsKeys.getInt(1); // bill_id is auto_increment
            }
            rsKeys.close();
            psBill.close();
            
            // Insert bill items
            PreparedStatement psItems = con.prepareStatement(
                "INSERT INTO bill_items (bill_id, product_id, quantity, price) VALUES (?, ?, ?, ?)"
            );
            
            for (int i = 0; i < billingModel.getRowCount(); i++) {
                String productId = billingModel.getValueAt(i, 0).toString();
                double price = (Double) billingModel.getValueAt(i, 2);
                int quantity = (Integer) billingModel.getValueAt(i, 3);
                
                psItems.setInt(1, billId);
                psItems.setString(2, productId);
                psItems.setInt(3, quantity);
                psItems.setDouble(4, price);
                psItems.addBatch();
                
                // Update product quantity
                PreparedStatement psUpdate = con.prepareStatement(
                    "UPDATE products SET quantity = quantity - ? WHERE id = ?"
                );
                psUpdate.setInt(1, quantity);
                psUpdate.setString(2, productId);
                psUpdate.executeUpdate();
                psUpdate.close();
            }
            
            psItems.executeBatch();
            psItems.close();
            
            con.commit();
            con.setAutoCommit(true);
            con.close();
            
            JOptionPane.showMessageDialog(this, 
                "Bill saved successfully!\nBill ID: " + billId + "\nGrand Total: ₹" + String.format("%.2f", grandTotal) + "\nGST: ₹" + String.format("%.2f", calculateGST()),
                "Success", 
                JOptionPane.INFORMATION_MESSAGE
            );
            
            int printChoice = JOptionPane.showConfirmDialog(this, "Do you want to print the bill as PDF?", "Print Bill", JOptionPane.YES_NO_OPTION);
            if (printChoice == JOptionPane.YES_OPTION) {
                generatePDF(billId, customerName);
            }
            
            clearBill();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving bill: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generatePDF() {
        if (billingModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please add products to the bill first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String customerName = customerField.getText().trim();
        if (customerName.isEmpty()) {
            customerName = "Guest";
        }
        generatePDF(0, customerName); // 0 means unsaved or preview
    }

    private void generatePDF(int billId, String customerName) {
        try {
            String fileName = "Invoice_" + (billId > 0 ? billId : System.currentTimeMillis()) + ".pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Header
            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22);
            Paragraph title = new Paragraph("SHOP APP INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Customer Info
            document.add(new Paragraph("Bill ID: " + (billId > 0 ? billId : "PREVIEW")));
            document.add(new Paragraph("Customer Name: " + customerName));
            document.add(new Paragraph("Date: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));
            document.add(new Paragraph("\n"));

            // Table
            PdfPTable pdfTable = new PdfPTable(5);
            pdfTable.setWidthPercentage(100);
            String[] headers = {"Product ID", "Name", "Price", "Qty", "Total"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                pdfTable.addCell(cell);
            }

            for (int i = 0; i < billingModel.getRowCount(); i++) {
                for (int j = 0; j < 5; j++) {
                    PdfPCell cell = new PdfPCell(new Phrase(billingModel.getValueAt(i, j).toString()));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfTable.addCell(cell);
                }
            }
            document.add(pdfTable);
            document.add(new Paragraph("\n"));

            // Summary
            Paragraph summary = new Paragraph();
            summary.setAlignment(Element.ALIGN_RIGHT);
            summary.add("Total Amount: Rs " + String.format("%.2f", totalAmount) + "\n");
            summary.add("GST (18%): Rs " + String.format("%.2f", calculateGST()) + "\n");
            com.itextpdf.text.Font grandTotalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            summary.add(new Phrase("Grand Total: Rs " + String.format("%.2f", grandTotal), grandTotalFont));
            
            document.add(summary);
            document.close();

            JOptionPane.showMessageDialog(this, "PDF Invoice Generated Successfully: " + fileName, "Success", JOptionPane.INFORMATION_MESSAGE);

            // Open PDF
            File pdfFile = new File(fileName);
            if (pdfFile.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
