package helper;

//This class is used to make a database connection.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;




public class dataBaseConnection
{

    public static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users//anton//Desktop//db//sample.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


}
