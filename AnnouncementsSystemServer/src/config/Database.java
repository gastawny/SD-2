package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    private static Connection conn = null;

    public static Connection connect() {
        if(conn != null) return conn;

        try {
            Properties props = loadProperties();
            String url = props.getProperty("dburl");

            conn = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            System.err.println("ERR: Connection to the database: " + e.getLocalizedMessage());
        }

        return conn;
    }

    public static void disconnect() {
        if(conn == null) return;

        try {
            conn.close();
            conn = null;
        } catch (SQLException e) {
            System.err.println("ERR: Disconnect from the database: " + e.getLocalizedMessage());
        }

    }

    private static Properties loadProperties() {
        Properties props = new Properties();

        props.setProperty("user", "docker");
        props.setProperty("password", "a8s45d18");
        props.setProperty("dburl", "jdbc:mysql://localhost:3308/sd");
        props.setProperty("useSSL", "false");
        props.setProperty("allowPublicKeyRetrieval", "true");

        return props;
    }
}
