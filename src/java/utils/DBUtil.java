package utils;

import java.sql.*;

public class DBUtil {

    // SQL Server connection
    // Đổi thông tin kết nối theo cấu hình SQL Server local của bạn
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=ECS;encrypt=false;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "your_password_here";  // TODO: Đổi password SQL Server của bạn

    public static Connection getConnection() throws Exception {
        Class.forName(DRIVER);
        return DriverManager.getConnection(URL, USER, PASS);
    }

}
