package connection;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {


public static Connection connect(){
    Connection conn= null;
    try {
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/labMap", "postgres", "password");


//        String url = "jdbc:postgresql://localhost:5432/labMap";
//        Properties props = new Properties();
//        props.setProperty("user","postgres");
//        props.setProperty("password","password");
//        props.setProperty("ssl","true");
//        conn = DriverManager.getConnection(url, props);

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return conn;
}



}







