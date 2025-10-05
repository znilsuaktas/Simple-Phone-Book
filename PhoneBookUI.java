import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PhoneBookUI extends JFrame {
    protected ContactDAO dao;
    protected JTextField nameField, phoneField, searchField;
    protected JTable table;
    protected DefaultTableModel tableModel;

    public PhoneBookUI() {
        dao = new ContactDAO();

        setTitle("Phone Book");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

       
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(230, 230, 250));

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(new Color(25, 25, 112));
        nameField = new JTextField(10);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(new Color(25, 25, 112));
        phoneField = new JTextField(10);

        JButton addBtn = new JButton("Add");
        addBtn.setBackground(new Color(60, 179, 113));
        addBtn.setForeground(Color.WHITE);

        topPanel.add(nameLabel);
        topPanel.add(nameField);
        topPanel.add(phoneLabel);
        topPanel.add(phoneField);
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

       
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Phone"}, 0);
        table = new JTable(tableModel);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setBackground(new Color(100, 149, 237));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

      
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(245, 245, 245));

        JLabel searchLabel = new JLabel("Search (Name/Phone):");
        searchLabel.setForeground(Color.DARK_GRAY);

        searchField = new JTextField(12);
        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(100, 149, 237));
        searchBtn.setForeground(Color.WHITE);

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setBackground(new Color(220, 20, 60));
        deleteBtn.setForeground(Color.WHITE);

        JButton updateBtn = new JButton("Update Phone");
        updateBtn.setBackground(new Color(60, 179, 113));
        updateBtn.setForeground(Color.WHITE);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBackground(new Color(255, 215, 0));
        refreshBtn.setForeground(Color.BLACK);

        bottomPanel.add(searchLabel);
        bottomPanel.add(searchField);
        bottomPanel.add(searchBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(updateBtn);
        bottomPanel.add(refreshBtn);
        add(bottomPanel, BorderLayout.SOUTH);

       
        addBtn.addActionListener((ActionEvent e) -> addContact());
        searchBtn.addActionListener((ActionEvent e) -> searchContacts());
        deleteBtn.addActionListener((ActionEvent e) -> deleteSelected());
        updateBtn.addActionListener((ActionEvent e) -> updatePhone());
        refreshBtn.addActionListener((ActionEvent e) -> loadAllContacts());

      
        loadAllContacts();

       
        JOptionPane.showMessageDialog(
                this,
                "Welcome to Phone Book!",
                "Welcome",
                JOptionPane.INFORMATION_MESSAGE
        );

        setVisible(true);
    }

    
    private void addContact() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and phone cannot be empty!");
            return;
        }

        if (phone.length() != 10 || !phone.matches("\\d+")) {
            JOptionPane.showMessageDialog(this,
                    "Phone number must be 10 digits! You entered an invalid phone number!",
                    "Invalid Phone Number",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        dao.addContact(name, phone);
        loadAllContacts();
        nameField.setText("");
        phoneField.setText("");
    }

    
    private void searchContacts() {
        String keyword = searchField.getText().trim();
        tableModel.setRowCount(0);
        List<Contact> results = dao.searchByKeyword(keyword);
        for (Contact c : results) {
            tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getPhone()});
        }
    }

   
    private void deleteSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            dao.deleteById(id);
            loadAllContacts();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete!");
        }
    }

  
    private void updatePhone() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String currentPhone = (String) tableModel.getValueAt(selectedRow, 2);

            String newPhone = JOptionPane.showInputDialog(
                    this,
                    "Enter new phone number:",
                    currentPhone
            );

            if (newPhone != null && !newPhone.trim().isEmpty()) {
                if (newPhone.length() != 10 || !newPhone.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits!");
                    return;
                }

                boolean success = dao.updatePhone(id, newPhone);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Phone updated successfully!");
                    loadAllContacts();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update phone number.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to update!");
        }
    }

    
    private void loadAllContacts() {
        tableModel.setRowCount(0);
        List<Contact> all = dao.getAllContacts();
        for (Contact c : all) {
            tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getPhone()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PhoneBookUI::new);
    }
}
