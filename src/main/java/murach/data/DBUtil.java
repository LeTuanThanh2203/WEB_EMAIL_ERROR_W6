package murach.data;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBUtil {

    private static final EntityManagerFactory emf;

    static {
        Map<String, String> props = new HashMap<>();

        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        // Local fallback
        if (dbUrl == null || dbUrl.trim().isEmpty()) {
            dbUrl = "jdbc:postgresql://localhost:5432/MurachSQLGateway";
        }

        if (dbUser == null || dbUser.trim().isEmpty()) {
            dbUser = "postgres";
        }

        // Không hardcode password
        if (dbPassword == null || dbPassword.trim().isEmpty()) {
            throw new IllegalStateException("DB_PASSWORD is not configured");
        }

        props.put("jakarta.persistence.jdbc.url", dbUrl);
        props.put("jakarta.persistence.jdbc.user", dbUser);
        props.put("jakarta.persistence.jdbc.password", dbPassword);

        emf = Persistence.createEntityManagerFactory("emailListPU", props);
    }

    public static EntityManagerFactory getEmFactory() {
        return emf;
    }

    public static void closeStatement(Statement s) {
        try {
            if (s != null) {
                s.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void closePreparedStatement(Statement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}