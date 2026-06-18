package utils;

import java.sql.*;

public class DBUtil {

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://13.239.184.69:3306/ECS?useSSL=false&serverTimezone=UTC";
        String user = "user";
        String pass = "password";
        return DriverManager.getConnection(url, user, pass);
    }

}
