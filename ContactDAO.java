import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    private final String url = "jdbc:sqlite:C:/Users/Zeynep/Documents/sqlite/contacts.db";

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
            System.out.println("Hata (getAllContacts): " + e.getMessage());
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
                System.out.println("ID is not exist.");
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
