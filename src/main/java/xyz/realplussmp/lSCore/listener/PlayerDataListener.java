package xyz.realplussmp.lSCore.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.realplussmp.lSCore.manager.ConfigManager;
import xyz.realplussmp.lSCore.manager.DataManager;
import xyz.realplussmp.lSCore.manager.HeartManager;

import java.util.UUID;

public class PlayerDataListener implements Listener {

    private final HeartManager heartManager;
    private final DataManager dataManager;
    private final ConfigManager config;

    public PlayerDataListener(HeartManager heartManager, DataManager dataManager, ConfigManager config) {
        this.heartManager = heartManager;
        this.dataManager = dataManager;
        this.config = config;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        int hearts = dataManager.loadHearts(uuid, config.getDefaultHearts());
        event.getPlayer().sendMessage("you have " + hearts);

        heartManager.load(uuid, hearts);
        heartManager.syncHealth(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        int hearts = heartManager.getHearts(uuid);
        dataManager.saveHearts(uuid, hearts);

        heartManager.unload(uuid);
    }
}
