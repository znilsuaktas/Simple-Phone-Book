import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {

 
    private final String url = "jdbc:sqlite:contacts.db";

  
    public ContactDAO() {
        initContactsTable();
        initCountryCodes();  
    }

   
    private void initContactsTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS contacts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "phone TEXT NOT NULL)";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTable);
        } catch (SQLException e) {
            System.out.println("Error (initContactsTable): " + e.getMessage());
        }
    }


    private void initCountryCodes() {
        String createTable = "CREATE TABLE IF NOT EXISTS country_codes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "code TEXT NOT NULL, " +
                "country TEXT)";
        String insertDefaults = "INSERT INTO country_codes (code, country) " +
                "SELECT '+90','Turkey' UNION ALL " +
                "SELECT '+1','United States' UNION ALL " +
                "SELECT '+44','United Kingdom' UNION ALL " +
                "SELECT '+49','Germany' UNION ALL " +
                "SELECT '+33','France' UNION ALL " +
                "SELECT '+34','Spain' UNION ALL " +
                "SELECT '+39','Italy' UNION ALL " +
                "SELECT '+31','Netherlands' UNION ALL " +
                "SELECT '+32','Belgium' UNION ALL " +
                "SELECT '+41','Switzerland' UNION ALL " +
                "SELECT '+43','Austria' UNION ALL " +
                "SELECT '+46','Sweden' UNION ALL " +
                "SELECT '+47','Norway' UNION ALL " +
                "SELECT '+45','Denmark' UNION ALL " +
                "SELECT '+358','Finland' UNION ALL " +
                "SELECT '+48','Poland' UNION ALL " +
                "SELECT '+420','Czech Republic' UNION ALL " +
                "SELECT '+36','Hungary' UNION ALL " +
                "SELECT '+351','Portugal' UNION ALL " +
                "SELECT '+30','Greece' UNION ALL " +
                "SELECT '+380','Ukraine' UNION ALL " +
                "SELECT '+7','Russia' UNION ALL " +
                "SELECT '+86','China' UNION ALL " +
                "SELECT '+81','Japan' UNION ALL " +
                "SELECT '+82','South Korea' UNION ALL " +
                "SELECT '+61','Australia' UNION ALL " +
                "SELECT '+64','New Zealand' UNION ALL " +
                "SELECT '+55','Brazil' UNION ALL " +
                "SELECT '+52','Mexico' UNION ALL " +
                "SELECT '+91','India' " +
                "WHERE NOT EXISTS (SELECT 1 FROM country_codes);";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTable);
            stmt.executeUpdate(insertDefaults);
        } catch (SQLException e) {
            System.out.println("Error (initCountryCodes): " + e.getMessage());
        }
    }

    
    public List<String> getCountryCodes() {
        List<String> codes = new ArrayList<>();
        String sql = "SELECT code || ' (' || country || ')' as display FROM country_codes";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                codes.add(rs.getString("display"));
            }
        } catch (SQLException e) {
            System.out.println("Error (getCountryCodes): " + e.getMessage());
        }
        return codes;
    }


    public void addContact(String name, String phone) {
        String sql = "INSERT INTO contacts(name, phone) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.executeUpdate();
            System.out.println("Contact added successfully");
        } catch (SQLException e) {
            System.out.println("Error (addContact): " + e.getMessage());
        }
    }

  
    public List<Contact> getAllContacts() {
        List<Contact> list = new ArrayList<>();
        String sql = "SELECT * FROM contacts";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone")));
            }
        } catch (SQLException e) {
            System.out.println("Error (getAllContacts): " + e.getMessage());
        }
        return list;
    }

 
    public List<Contact> searchByKeyword(String keyword) {
        List<Contact> list = new ArrayList<>();
        String sql = "SELECT * FROM contacts WHERE name LIKE ? OR phone LIKE ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error (searchByKeyword): " + e.getMessage());
        }
        return list;
    }

   
    public void deleteById(int id) {
        String sql = "DELETE FROM contacts WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Contact deleted successfully");
            } else {
                System.out.println("ID does not exist.");
            }
        } catch (SQLException e) {
            System.out.println("Error (deleteById): " + e.getMessage());
        }
    }


    public boolean updatePhone(int id, String newPhone) {
        String sql = "UPDATE contacts SET phone = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPhone);
            pstmt.setInt(2, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error (updatePhone): " + e.getMessage());
            return false;
        }
    }
}
