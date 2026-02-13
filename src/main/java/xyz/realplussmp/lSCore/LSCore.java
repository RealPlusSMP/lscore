package xyz.realplussmp.lSCore;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.realplussmp.lSCore.commands.ReviveCommand;
import xyz.realplussmp.lSCore.item.HeartItem;
import xyz.realplussmp.lSCore.listener.DeathListener;
import xyz.realplussmp.lSCore.listener.PlayerDataListener;
import xyz.realplussmp.lSCore.manager.ConfigManager;
import xyz.realplussmp.lSCore.manager.DataManager;
import xyz.realplussmp.lSCore.manager.HeartManager;

public final class LSCore extends JavaPlugin {

    private ConfigManager config;
    private DataManager dataManager;
    private HeartManager heartManager;

    @Override
    public void onEnable() {
        // load config
        config = new ConfigManager(this);
        config.load();

        // init database
        dataManager = new DataManager(getDataFolder());
        dataManager.init();

        // init heart manager
        heartManager = new HeartManager(
                config.getMinHearts(),
                config.getMaxHearts()
        );

        // harditem
        HeartItem.init(this);

        getServer().getPluginManager().registerEvents(new PlayerDataListener(heartManager, dataManager, config), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this, heartManager, dataManager, config), this);

        // comand
        getCommand("revive").setExecutor(new ReviveCommand(heartManager, dataManager, config));
        getCommand("revive").setTabCompleter(new ReviveCommand(heartManager, dataManager, config));

        getLogger().info("LifeSteal Core enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("LifeSteal Core disabled!");
    }
}
