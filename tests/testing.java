
import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by tristangreeno on 4/27/16.
 */
public class testing {

    @Test
    public void insertUser() throws SQLException {
        Connection conn = Sql.startConnection();

        String name = "James";

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?)");
        stmt.setString(1, name);

        assert stmt.execute();
    }

    @Test
    public void selectUser() throws SQLException {
        Connection conn = Sql.startConnection();
        Sql.insertUser(conn, "James");

        String name = "James";
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        stmt.setString(1, "James");
        ResultSet set = stmt.executeQuery();

        set.next();
        User user = new User(set.getInt("id"), set.getString("name"));

        Assert.assertEquals((Integer) 1, user.id);
    }

    @Test
    public void insertItem() throws SQLException {
        Connection conn = Sql.startConnection();
        Integer userId = 1;
        String text = "Text";

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO items VALUES (?, ?, ?)");
        stmt.setInt(1, userId);
        stmt.setString(2, text);
        stmt.setBoolean(3, false);

        assert stmt.execute();

    }

    @Test
    public void selectItems() throws SQLException {
        Connection conn = Sql.startConnection();
        Sql.insertUser(conn, "James");
        Sql.insertItem(conn, 1, "texty");
        ArrayList<Item> items = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM items INNER JOIN users " +
                "ON items.user_id = users.id");
        ResultSet set = stmt.executeQuery();

        while (set.next()){
            items.add(new Item(set.getString("items.text"), set.getBoolean("items.is_done"),
                    set.getInt("items.user_id"), set.getInt("items.id")));
        }

        Assert.assertNotNull(items);
    }

    @Test
    public void selectItem() throws SQLException {
        Connection conn = Sql.startConnection();
        Sql.insertUser(conn, "James");
        Sql.insertItem(conn, 1, "text");

        Integer itemId = 1;
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM items INNER JOIN users ON items.user_id = " +
                "users.id WHERE items.id = ?");
        stmt.setInt(1, itemId);
        ResultSet set = stmt.executeQuery();

        set.next();
        Integer userId = set.getInt("items.user_id");
        String text = set.getString("items.text");
        Boolean isDone = set.getBoolean("items.is_done");
        Item item = new Item(text, isDone, userId, itemId);


        Assert.assertNotNull(item);
    }

    @Test
    public void updateItem() throws SQLException {
        Connection conn = Sql.startConnection();
        Sql.insertUser(conn, "James");
        Sql.insertItem(conn, 1, "text");
        Item item = Sql.selectItem(conn, 1);

        if(item.isDone) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE items SET is_done = TRUE WHERE id = ?");
            stmt.setInt(1, item.itemId);
            assert stmt.execute();
        }
        else {
            PreparedStatement stmt = conn.prepareStatement("UPDATE items SET is_done = FALSE WHERE id = ?");
            stmt.setInt(1, item.itemId);
            assert stmt.execute();
        }
    }

    @Test
    public void deleteItem() throws SQLException {
        Connection conn = Sql.startConnection();
        Sql.insertUser(conn, "James");
        Sql.insertItem(conn, 1, "text");
        Integer itemId = 1;

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM items WHERE id = ?");
        stmt.setInt(1, itemId);
        assert stmt.execute();
    }
}
