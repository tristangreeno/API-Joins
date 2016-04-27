import org.junit.Assert;
import org.junit.Test;

import java.sql.*;

/**
 * Created by tristangreeno on 4/27/16.
 */
public class testing {
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
        stmt.execute("CREATE TABLE items (userId NUMBER, text VARCHAR, is_done VARCHAR)");
    }

    @Test
    public void insertUser() throws SQLException {
        Connection conn = startConnection();
        createTables(conn);

        String name = "James";

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?)");
        stmt.setString(1, name);

        assert stmt.execute();
    }

    @Test
    public void selectUser() throws SQLException {
        Connection conn = startConnection();
        Integer id = 1;
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet set = stmt.executeQuery();

        set.next();
         =
    }
}
