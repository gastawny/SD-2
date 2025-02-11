package repositories;

import config.Database;
import models.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    public static void create(List<Category> categoriesList) throws SQLException {
        String sql = "INSERT INTO category (name, description) VALUES (?, ?)";

        try (Connection conn = Database.connect()) {
            conn.setAutoCommit(false);

            for (Category category : categoriesList) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, category.name());
                    ps.setString(2, category.description());

                    ps.executeUpdate();
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                }
            }

            conn.commit();
        } finally {
            Database.disconnect();
        }
    }

    public static Category read(String categoryId) throws SQLException {
        String sql = "SELECT * FROM category WHERE category_id = ?";

        try (Connection conn = Database.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, categoryId);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    categoryId = rs.getString("category_id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");

                    return new Category(categoryId, name, description);
                }

                return null;
            }
        } finally {
            Database.disconnect();
        }
    }

    public static List<Category> readAll() throws SQLException {
        String sql = "SELECT * FROM category";

        try (Connection conn = Database.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                List<Category> categoryList = new ArrayList<>();

                while (rs.next()) {
                    String categoryId = rs.getString("category_id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");

                    categoryList.add(new Category(categoryId, name, description));
                }

                return categoryList;
            }
        } finally {
            Database.disconnect();
        }
    }

    public static void update(List<Category> categoriesList) throws SQLException {
        try (Connection conn = Database.connect()) {
            conn.setAutoCommit(false);

            for (Category category : categoriesList) {
                if(!category.name().isBlank())
                    updateName(conn, category);

                if(!category.description().isBlank())
                    updateDescription(conn, category);
            }

            conn.commit();
        } finally {
            Database.disconnect();
        }
    }

    private static void updateName(Connection conn, Category category) throws SQLException {
        String sql = "UPDATE category SET name = ? WHERE category_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.name());
            ps.setString(2, category.id());

            ps.executeUpdate();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    private static void updateDescription(Connection conn, Category category) throws SQLException {
        String sql = "UPDATE category SET description = ? WHERE category_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.description());
            ps.setString(2, category.id());

            ps.executeUpdate();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public static void delete(List<String> categoryIdsList) throws SQLException {
        String sql = "DELETE FROM category WHERE category_id = ?";

        try (Connection conn = Database.connect()) {
            conn.setAutoCommit(false);

            for (String categoryId : categoryIdsList) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, categoryId);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                }
            }

            conn.commit();
        } finally {
            Database.disconnect();
        }
    }
}
