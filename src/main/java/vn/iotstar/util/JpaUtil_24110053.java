package vn.iotstar.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public final class JpaUtil_24110053 {
    private static final EntityManagerFactory FACTORY = createFactory();
    private JpaUtil_24110053() {}

    private static EntityManagerFactory createFactory() {
        Map<String, Object> properties = new HashMap<>();
        String defaultUrl = "jdbc:sqlserver://localhost:1433;databaseName=BaiTapGiuaKy;encrypt=false;trustServerCertificate=true;integratedSecurity=true";
        properties.put("jakarta.persistence.jdbc.url", env("DB_URL", defaultUrl));
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        if (user != null && !user.isBlank()) properties.put("jakarta.persistence.jdbc.user", user);
        if (password != null) properties.put("jakarta.persistence.jdbc.password", password);
        return Persistence.createEntityManagerFactory("BaiTapGiuaKyPU", properties);
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }

    public static EntityManager entityManager() { return FACTORY.createEntityManager(); }
    public static void close() { if (FACTORY.isOpen()) FACTORY.close(); }
}
