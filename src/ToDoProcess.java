import java.sql.Connection;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by tristangreeno on 4/4/16.
 */
public class ToDoProcess {

    private static Scanner scanOpt = new Scanner(System.in);
    private static Scanner scanNew = new Scanner(System.in);

    public static void toDoProcess() throws Exception {
        Connection conn = Sql.startConnection();
        System.out.println("Please enter your username: ");
        String username = scanOpt.nextLine();
        Sql.insertUser(conn, username);
        User user = Sql.selectUser(conn, username);

        while (true) {
            System.out.println("[1] Add to list; [2] update item status; [3] view list; [4] delete item");

            String option = scanOpt.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Enter your TODO item: ");
                    String text = scanOpt.nextLine();

                    Sql.insertItem(conn, user.id, text);
                    break;
                case "2":
                    try {
                        System.out.println("Enter the ID of the item to be modified: ");
                        Integer itemNum = Integer.parseInt(scanNew.nextLine());
                        Sql.updateItem(conn, itemNum);
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Must be integer.");
                    }
                    break;
                case "3":
                    ArrayList<Item> items = Sql.selectItems(conn);
                    int i = 0;
                    for (Item itemz : items) {
                        if(!itemz.isDone) {
                            System.out.println("[ ] " + itemz.text + " - ID: " + itemz.itemId);
                        }

                        else { System.out.println("[X] " + itemz.text + " - ID: " + itemz.itemId); }
                    }
                    break;
                case "4":
                    System.out.println("Enter the ID of the item to be deleted: ");
                    Integer itemNumber = Integer.parseInt(scanNew.nextLine());
                    Sql.deleteItem(conn, itemNumber);
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }
}
