package murach.data;

import java.sql.*;

public class ConnectionPool {

    private static ConnectionPool pool = null;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    private ConnectionPool() {
        dbUrl = System.getenv("DB_URL");
        dbUser = System.getenv("DB_USER");
        dbPassword = System.getenv("DB_PASSWORD");

        // Local fallback
        if (dbUrl == null || dbUrl.trim().isEmpty()) {
            dbUrl = "jdbc:postgresql://localhost:5432/MurachSQLGateway";
        }
        if (dbUser == null || dbUser.trim().isEmpty()) {
            dbUser = "postgres";
        }
        if (dbPassword == null || dbPassword.trim().isEmpty()) {
            System.out.println("Warning: DB_PASSWORD is not configured.");
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            System.err.println("DB_URL: " + dbUrl);
            System.err.println("DB_USER: " + dbUser);
            System.err.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void freeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}