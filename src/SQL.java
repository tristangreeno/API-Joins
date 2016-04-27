import java.sql.*;
import java.util.ArrayList;

/**
 * Created by tristangreeno on 4/27/16.
 */
public class Sql {
    public static Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        createTables(conn);
        return conn;
    }

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE IF EXISTS users");
        stmt.execute("DROP TABLE IF EXISTS items");
        stmt.execute("CREATE TABLE users (id IDENTITY, name VARCHAR)");
        stmt.execute("CREATE TABLE items (id IDENTITY, user_id INT, text VARCHAR, is_done VARCHAR)");
    }

    public static void insertUser(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?)");
        stmt.setString(1, name);
        stmt.execute();
    }

    public static User selectUser(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        stmt.setString(1, name);
        ResultSet set = stmt.executeQuery();
        set.next();

        return new User(set.getInt("id"), set.getString("name"));
    }

    public static Item selectItem(Connection conn, Integer itemId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM items INNER JOIN users ON items.user_id = " +
                "users.id WHERE items.id = ?");
        stmt.setInt(1, itemId);
        ResultSet set = stmt.executeQuery();
        set.next();

        Integer userId = set.getInt("items.user_id");
        String text = set.getString("items.text");
        Boolean isDone = set.getBoolean("items.is_done");
        return new Item(text, isDone, userId, itemId);
    }

    public static void insertItem(Connection conn, Integer userId, String text) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO items VALUES (NULL, ?, ?, ?)");
        stmt.setInt(1, userId);
        stmt.setString(2, text);
        stmt.setBoolean(3, false);

        stmt.execute();
    }

    public static ArrayList<Item> selectItems(Connection conn) throws SQLException {
        ArrayList<Item> items = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM items INNER JOIN users " +
                "ON items.user_id = users.id");
        ResultSet set = stmt.executeQuery();

        while (set.next()) {
            items.add(new Item(set.getString("items.text"), set.getBoolean("items.is_done"),
                    set.getInt("items.user_id"), set.getInt("items.id")));
        }

        return items;
    }

    public static void updateItem(Connection conn, Integer itemId) throws SQLException{
        Item item = Sql.selectItem(conn, itemId);

        if (! item.isDone) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE items SET is_done = TRUE WHERE id = ?");
            stmt.setInt(1, item.itemId);
            stmt.execute();
        } else {
            PreparedStatement stmt = conn.prepareStatement("UPDATE items SET is_done = FALSE WHERE id = ?");
            stmt.setInt(1, item.itemId);
            stmt.execute();
        }
    }

    public static void deleteItem(Connection conn, Integer itemId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM items WHERE id = ?");
        stmt.setInt(1, itemId);
        stmt.execute();
    }
}
