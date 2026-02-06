package xyz.realplussmp.lSCore.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.realplussmp.lSCore.manager.DataManager;
import xyz.realplussmp.lSCore.manager.HeartManager;

import java.util.UUID;

public class DeathListener implements Listener {

    private final HeartManager heartManager;
    private final DataManager dataManager;

    private final int stealAmount;
    private final int minHearts;
    private final boolean banOnMinHearts;

    private final JavaPlugin plugin;

    public DeathListener(JavaPlugin plugin, HeartManager heartManager, DataManager dataManager, int stealAmount, boolean banOnMinHearts, int minHearts) {
        this.plugin = plugin;
        this.heartManager = heartManager;
        this.dataManager = dataManager;
        this.stealAmount = stealAmount;
        this.banOnMinHearts = banOnMinHearts;
        this.minHearts = minHearts;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer == null) return;
        if (killer.getUniqueId().equals(victim.getUniqueId())) return;

        UUID victimUUID = victim.getUniqueId();

        // remove hearts from victim
        heartManager.removeHearts(victimUUID, stealAmount);

        // drop hearts at victim's location
        // TODO: implement item for heart
        for (int i = 0; i < stealAmount; i++) {
            victim.getWorld().dropItemNaturally(victim.getLocation(), new ItemStack(Material.STONE, 1));
        }

        // sync health
        heartManager.syncHealth(victim);

        // save instantly
        dataManager.saveHearts(victimUUID, heartManager.getHearts(victimUUID));

        // elimination check
        if (banOnMinHearts) {
            int victimHearts = heartManager.getHearts(victimUUID);

            if (victimHearts <= minHearts) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> eliminate(victim), 20L);
            }
        }
    }

    private void eliminate(Player victim) {
        // TODO: think of what todo when victim is eliminate
    }
}