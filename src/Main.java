import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
            String URL = "jdbc:mysql://localhost:3306/db-nations";
            String USER = "root" ;
            String PASSWORD = "root";

            try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){
                System.out.println(conn.isClosed());
            } catch (SQLException e) {
                e.printStackTrace();
            }


    }
}