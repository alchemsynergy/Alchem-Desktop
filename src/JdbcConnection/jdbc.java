package JdbcConnection;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by VIPUL GOYAL on 9/9/2017.
 */
public class jdbc {
    public static Connection jconn()
    {
        Connection conn=null;
        try
        {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/AlchemDB","postgres","123456");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return conn;
    }
}
