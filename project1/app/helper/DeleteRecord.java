package helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static helper.dataBaseConnection.connect;

//This class is used to handle deleting database enteries.
public class DeleteRecord
{
    public static void deleteRecordFromTable (String name) throws SQLException
    {
        Connection c =connect();

        String sql = "DELETE FROM people WHERE"
                + " people.name =?"
                ;
        try
        {

            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.executeUpdate();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally{
            c.close();
        }

    }
}
