import java.util.List;
import java.util.Scanner;

public class Main {

   
    public static final String RESET  = "\u001B[0m";
    public static final String BLACK  = "\u001B[30m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN   = "\u001B[36m";
    public static final String WHITE  = "\u001B[37m";

    public static void main(String[] args) {
        ContactDAO dao = new ContactDAO();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println(BLUE + "\n=== Phone Book ===" + RESET);
            System.out.println(GREEN + "1 - Add new contact" + RESET);
            System.out.println(GREEN + "2 - List all contacts" + RESET);
            System.out.println(GREEN + "3 - Search by name or phone" + RESET);
            System.out.println(GREEN + "4 - Delete by ID" + RESET);
            System.out.println(RED   + "0 - Exit" + RESET);
            System.out.print(CYAN  + "Your choice: " + RESET);
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Phone: ");
                    String phone = scanner.nextLine();
                    dao.addContact(name, phone);
                    break;

                case 2:
                    List<Contact> all = dao.getAllContacts();
                    if (all.isEmpty()) {
                        System.out.println(YELLOW + "The phone book is empty." + RESET);
                    } else {
                        System.out.println(PURPLE + "--- All Contacts ---" + RESET);
                        all.forEach(System.out::println);
                    }
                    break;

                case 3:
                    System.out.print("Enter name or phone to search: ");
                    String keyword = scanner.nextLine();
                    List<Contact> results = dao.searchByKeyword(keyword);
                    if (results.isEmpty()) {
                        System.out.println(YELLOW + "No results found." + RESET);
                    } else {
                        System.out.println(PURPLE + "--- Search Results ---" + RESET);
                        results.forEach(System.out::println);
                    }
                    break;

                case 4:
                    System.out.print("Enter ID to delete: ");
                    int id = scanner.nextInt();
                    dao.deleteById(id);
                    break;

                case 0:
                    System.out.println(RED + "Exiting the program..." + RESET);
                    break;

                default:
                    System.out.println(YELLOW + "Invalid choice!" + RESET);
            }
        } while (choice != 0);

        scanner.close();
    }
}
