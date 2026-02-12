package xyz.realplussmp.lSCore.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.realplussmp.lSCore.manager.ConfigManager;
import xyz.realplussmp.lSCore.manager.DataManager;
import xyz.realplussmp.lSCore.manager.HeartManager;

import java.time.Duration;
import java.util.UUID;

public class DeathListener implements Listener {

    private final JavaPlugin plugin;
    private final HeartManager heartManager;
    private final DataManager dataManager;
    private final ConfigManager config;

    public DeathListener(JavaPlugin plugin, HeartManager heartManager, DataManager dataManager, ConfigManager config) {
        this.plugin = plugin;
        this.heartManager = heartManager;
        this.dataManager = dataManager;
        this.config = config;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        UUID victimUUID = victim.getUniqueId();

        int minHearts = config.getMinHearts();

        // remove hearts from victim
        heartManager.removeHearts(victimUUID, 1);

        // drop hearts at victim's location
        // TODO: implement item for heart
        for (int i = 0; i < 1; i++) {
            victim.getWorld().dropItemNaturally(victim.getLocation(), new ItemStack(Material.STONE, 1));
        }

        // sync health
        heartManager.syncHealth(victim);

        // save instantly
        dataManager.saveHearts(victimUUID, heartManager.getHearts(victimUUID));

        // elimination check
        int victimHearts = heartManager.getHearts(victimUUID);

        // victimhearts can't get below 1 so this is a shitty fix.
        if (victimHearts == minHearts) {
            eliminate(victim);
        }
    }

    private void eliminate(Player victim) {
        // TODO: make this grab the ban duration from configuration
        victim.banIp(config.getBanReason(), Duration.ofDays(1), null, true);
    }
}
