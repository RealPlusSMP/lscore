package xyz.realplussmp.lSCore.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
    private final JavaPlugin plugin;

    private int defaultHearts;
    private int minHearts;
    private int maxHearts;

    private String banReason;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        FileConfiguration config = plugin.getConfig();

        // hearts
        this.defaultHearts = config.getInt("hearts.default", 10);
        this.minHearts = config.getInt("hearts.min", 1);
        this.maxHearts = config.getInt("hearts.max", 20);

        // punishments
        this.banReason = config.getString("punishments.ban-reason", "<red>You ran out of hearts.");
    }

    public void reload() {
        load();
    }

    public int getDefaultHearts() {
        return defaultHearts;
    }

    public int getMinHearts() {
        return minHearts;
    }

    public int getMaxHearts() {
        return maxHearts;
    }

    public String getBanReason() {
        return banReason;
    }
}
