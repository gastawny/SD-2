package repositories;

import config.Database;
import models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRepository {

    public void create(String userId, String name, String password) throws SQLException {
        try (Connection conn = Database.connect()) {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO user (user_id, name, password) VALUES (?, ?, ?)")) {
                ps.setString(1, userId);
                ps.setString(2, name);
                ps.setString(3, password);

                ps.executeUpdate();
            }
        } finally {
            Database.disconnect();
        }
    }

    public Account getById(String id) throws SQLException {
        try (Connection conn = Database.connect()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE user_id = ?")) {
                ps.setString(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new Account(
                            id,
                            rs.getString("password"),
                            rs.getString("name"),
                            rs.getBoolean("is_admin")
                    );
                }

                return null;
            }
        } finally {
            Database.disconnect();
        }
    }

    public void updateName(String userId, String name) throws SQLException {
        try (Connection conn = Database.connect()) {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE user SET name = ? WHERE user_id = ?")) {
                ps.setString(1, name);
                ps.setString(2, userId);

                ps.executeUpdate();
            }
        } finally {
            Database.disconnect();
        }
    }

    public void updatePassword(String userId, String password) throws SQLException {
        try (Connection conn = Database.connect()) {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE user SET password = ? WHERE user_id = ?")) {
                ps.setString(1, password);
                ps.setString(2, userId);

                ps.executeUpdate();
            }
        } finally {
            Database.disconnect();
        }
    }

    public void delete(String id) throws SQLException {
        try (Connection conn = Database.connect()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM user WHERE user_id = ?")) {
                ps.setString(1, id);
                ps.executeUpdate();
            }
        } finally {
            Database.disconnect();
        }
    }
}
