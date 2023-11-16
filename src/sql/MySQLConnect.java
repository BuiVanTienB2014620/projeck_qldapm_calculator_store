package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnect {
    private static String url = "jdbc:mysql://localhost:3306/calculatorStore";
    private static String username = "root";
    private static String password = "";
    private static Connection con;

    public static Connection getConnection() {
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            // log an exception. fro example:
            ex.printStackTrace();
            System.out.println("Không kết nối dược với cơ sở dũ liệu.");
        }
        return con;
    }
}