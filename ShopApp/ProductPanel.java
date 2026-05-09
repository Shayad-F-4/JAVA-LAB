import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ProductPanel extends JPanel {

    JTextField id,name,price,qty,cat,search;
    JTable table;
    DefaultTableModel model;

    ProductPanel(){
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Initialize database and add discount column
        DBConnection.initializeDatabase();

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create styled text fields
        id=createStyledTextField(10);
        name=createStyledTextField(15);
        price=createStyledTextField(10);
        qty=createStyledTextField(10);
        cat=createStyledTextField(15);
        search=createStyledTextField(15);

        // Add form fields
        addFormField(formPanel, gbc, 0, 0, "Product ID:", id);
        addFormField(formPanel, gbc, 0, 2, "Name:", name);
        addFormField(formPanel, gbc, 1, 0, "Price:", price);
        addFormField(formPanel, gbc, 1, 2, "Quantity:", qty);
        addFormField(formPanel, gbc, 2, 0, "Category:", cat);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(UITheme.BG_COLOR);
        
        JButton addBtn = createThemedButton("Add Product", new Color(46, 204, 113));
        JButton updateBtn = createThemedButton("Update", new Color(52, 152, 219));
        JButton deleteBtn = createThemedButton("Delete", new Color(231, 76, 60));
        JButton clearBtn = createThemedButton("Clear Form", new Color(149, 165, 166));
        
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(UITheme.BG_COLOR);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(search);
        JButton searchBtn = createThemedButton("Search", new Color(52, 152, 219));
        searchPanel.add(searchBtn);

        // Combine form and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UITheme.BG_COLOR);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        topPanel.add(searchPanel, BorderLayout.NORTH);

        // Table
        model=new DefaultTableModel(
            new String[]{"ID","Name","Price","Quantity","Category"},0);
        table=new JTable(model);
        styleTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(UITheme.BG_COLOR);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadData();

        // Action Listeners
        addBtn.addActionListener(e->addProduct());
        updateBtn.addActionListener(e->updateProduct());
        deleteBtn.addActionListener(e->deleteProduct());
        clearBtn.addActionListener(e->clearForm());
        searchBtn.addActionListener(e->searchProduct());
        
        // Add price change listener
        price.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                // Discount removed
            }
        });
        
        // Table selection listener
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                fillFormFromTable();
            }
        });
        
        // Auto-refresh when tab is opened
        this.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                loadData();
            }
        });
    }
    
    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(UITheme.MAIN_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, int col, String label, JTextField field) {
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = col + 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }
    
    private JButton createThemedButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        button.setFont(UITheme.BOLD_FONT);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void styleTable(JTable table) {
        table.setFont(UITheme.MAIN_FONT);
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(Color.WHITE); // No selection color effect
        table.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = table.getTableHeader();
        header.setUI(new javax.swing.plaf.basic.BasicTableHeaderUI()); // Removes native Windows hover effects
        header.setFont(UITheme.BOLD_FONT);
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false);
    }

    void addProduct(){
        try{
            Connection con=DBConnection.getConnection();

            PreparedStatement ps=con.prepareStatement(
                "INSERT INTO products (id, name, price, quantity, category) VALUES(?,?,?,?,?)");

            ps.setInt(1,Integer.parseInt(id.getText()));
            ps.setString(2,name.getText());
            ps.setDouble(3,Double.parseDouble(price.getText()));
            ps.setInt(4,Integer.parseInt(qty.getText()));
            ps.setString(5,cat.getText());

            ps.executeUpdate();
            loadData();

        }catch(Exception e){e.printStackTrace();}
    }

    void updateProduct(){
        try{
            Connection con=DBConnection.getConnection();

            PreparedStatement ps=con.prepareStatement(
                "UPDATE products SET name=?,price=?,quantity=?,category=? WHERE id=?");

            ps.setString(1,name.getText());
            ps.setDouble(2,Double.parseDouble(price.getText()));
            ps.setInt(3,Integer.parseInt(qty.getText()));
            ps.setString(4,cat.getText());
            ps.setInt(5,Integer.parseInt(id.getText()));

            ps.executeUpdate();
            loadData();

        }catch(Exception e){e.printStackTrace();}
    }

    void deleteProduct(){
        try{
            int row=table.getSelectedRow();
            int idVal=(int)model.getValueAt(row,0);

            Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(
                "DELETE FROM products WHERE id=?");

            ps.setInt(1,idVal);
            ps.executeUpdate();

            loadData();

        }catch(Exception e){e.printStackTrace();}
    }

    void searchProduct(){
        try{
            model.setRowCount(0);

            Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(
                "SELECT * FROM products WHERE name LIKE ?");

            ps.setString(1,"%"+search.getText()+"%");
            ResultSet rs=ps.executeQuery();

            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("category")
                });
            }

        }catch(Exception e){e.printStackTrace();}
    }

    void loadData(){
        try{
            model.setRowCount(0);

            Connection con=DBConnection.getConnection();
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM products");

            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("category")
                });
            }

        }catch(Exception e){e.printStackTrace();}
    }
    
    private void calculateDiscount() {
        // Method removed
    }
    
    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row != -1) {
            id.setText(model.getValueAt(row, 0).toString());
            name.setText(model.getValueAt(row, 1).toString());
            price.setText(model.getValueAt(row, 2).toString());
            qty.setText(model.getValueAt(row, 3).toString());
            cat.setText(model.getValueAt(row, 4).toString());
        }
    }
    
    private void clearForm() {
        id.setText("");
        name.setText("");
        price.setText("");
        qty.setText("");
        cat.setText("");
        search.setText("");
    }
}