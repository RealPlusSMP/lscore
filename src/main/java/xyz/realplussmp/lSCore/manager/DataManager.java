package xyz.realplussmp.lSCore.manager;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class DataManager {

    private final File dbFile;

    public DataManager(File dataFolder) {
        this.dbFile = new File(dataFolder, "lifesteal.db");
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
    }

    public void init() {
        try {
            if (!dbFile.exists()) {
                dbFile.getParentFile().mkdirs();
                dbFile.createNewFile();
            }

            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {

                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS player_hearts (
                        uuid TEXT PRIMARY KEY,
                        hearts INTEGER NOT NULL
                    );
                """);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int loadHearts(UUID uuid, int defaultHearts) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT hearts FROM player_hearts WHERE uuid = ?"
             )) {

            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("hearts");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return defaultHearts;
    }

    public void saveHearts(UUID uuid, int hearts) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("""
                 INSERT INTO player_hearts(uuid, hearts)
                 VALUES(?, ?)
                 ON CONFLICT(uuid) DO UPDATE SET hearts = excluded.hearts;
             """)) {

            ps.setString(1, uuid.toString());
            ps.setInt(2, hearts);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasPlayer(UUID uuid) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT uuid FROM player_hearts WHERE uuid = ?"
             )) {

            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
