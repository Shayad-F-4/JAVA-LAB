import javax.swing.*;//
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerPanel extends JPanel {

    private JTextField idField, nameField, phoneField, searchField;
    private JTable table;
    private DefaultTableModel model;

    public CustomerPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = createStyledTextField(10);
        idField.setEditable(false); // Auto-increment in DB
        nameField = createStyledTextField(15);
        phoneField = createStyledTextField(15);
        searchField = createStyledTextField(15);

        addFormField(formPanel, gbc, 0, 0, "Customer ID:", idField);
        addFormField(formPanel, gbc, 0, 2, "Name:", nameField);
        addFormField(formPanel, gbc, 1, 0, "Phone:", phoneField);

        // Buttons
        JButton addBtn = createButton("Add Customer", UITheme.SUCCESS_COLOR);
        JButton updateBtn = createButton("Update", UITheme.PRIMARY_COLOR);
        JButton deleteBtn = createButton("Delete", UITheme.DANGER_COLOR);
        JButton clearBtn = createButton("Clear Form", UITheme.NEUTRAL_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.BG_COLOR);
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(UITheme.BG_COLOR);
        searchPanel.add(new JLabel("Search (Name/Phone):"));
        searchField.setPreferredSize(new Dimension(200, 25));
        searchPanel.add(searchField);

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(UITheme.BG_COLOR);
        topWrapper.add(formPanel, BorderLayout.CENTER);
        topWrapper.add(buttonPanel, BorderLayout.SOUTH);
        topWrapper.add(searchPanel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[] { "ID", "Name", "Phone" }, 0);
        table = new JTable(model);
        styleTable(table);

        add(topWrapper, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();

        // Listeners
        addBtn.addActionListener(e -> addCustomer());
        updateBtn.addActionListener(e -> updateCustomer());
        deleteBtn.addActionListener(e -> deleteCustomer());
        clearBtn.addActionListener(e -> clearForm());

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                searchCustomer();
            }
        });

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
    
    private void styleTable(JTable table) {
        table.setFont(UITheme.MAIN_FONT);
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(Color.WHITE); // No selection color effect
        table.setSelectionForeground(Color.BLACK);
        
        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setUI(new javax.swing.plaf.basic.BasicTableHeaderUI()); // Removes native Windows hover effects
        header.setFont(UITheme.BOLD_FONT);
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(UITheme.BOLD_FONT);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void addCustomer() {
        if (nameField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Phone are required!");
            return;
        }
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("INSERT INTO customers (name, phone) VALUES (?, ?)")) {
            ps.setString(1, nameField.getText());
            ps.setString(2, phoneField.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer Added!");
            clearForm();
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateCustomer() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a customer to update!");
            return;
        }
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("UPDATE customers SET name=?, phone=? WHERE id=?")) {
            ps.setString(1, nameField.getText());
            ps.setString(2, phoneField.getText());
            ps.setInt(3, Integer.parseInt(idField.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer Updated!");
            clearForm();
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteCustomer() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete!");
            return;
        }
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("DELETE FROM customers WHERE id=?")) {
            ps.setInt(1, Integer.parseInt(idField.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer Deleted!");
            clearForm();
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Cannot delete customer. They might be linked to existing bills.");
            ex.printStackTrace();
        }
    }

    private void searchCustomer() {
        String keyword = searchField.getText();
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con
                        .prepareStatement("SELECT * FROM customers WHERE name LIKE ? OR phone LIKE ?")) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[] { rs.getInt("id"), rs.getString("name"), rs.getString("phone") });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadData() {
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM customers")) {
            while (rs.next()) {
                model.addRow(new Object[] { rs.getInt("id"), rs.getString("name"), rs.getString("phone") });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row != -1) {
            idField.setText(model.getValueAt(row, 0).toString());
            nameField.setText(model.getValueAt(row, 1).toString());
            phoneField.setText(model.getValueAt(row, 2).toString());
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        searchField.setText("");
    }
}
