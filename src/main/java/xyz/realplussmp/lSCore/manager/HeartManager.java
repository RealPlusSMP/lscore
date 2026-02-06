package xyz.realplussmp.lSCore.manager;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HeartManager {
    private final Map<UUID, Integer> heartCache = new ConcurrentHashMap<>();

    private final int minHearts;
    private final int maxHearts;

    public HeartManager(int minHearts, int maxHearts) {
        this.minHearts = minHearts;
        this.maxHearts = maxHearts;
    }

    // cache

    public boolean isLoaded(UUID uuid) {
        return heartCache.containsKey(uuid);
    }

    public void load(UUID uuid, int hearts) {
        heartCache.put(uuid, clamp(hearts));
    }

    public void unload(UUID uuid) {
        heartCache.remove(uuid);
    }

    // logic

    public int getHearts(UUID uuid) {
        return heartCache.getOrDefault(uuid, minHearts);
    }

    public void setHearts(UUID uuid, int hearts) {
        heartCache.put(uuid, clamp(hearts));
        syncHealth(uuid);
    }

    public void addHearts(UUID uuid, int amount) {
        setHearts(uuid, getHearts(uuid) + amount);
    }

    public void removeHearts(UUID uuid, int amount) {
        setHearts(uuid, getHearts(uuid) - amount);
    }

    // sync health

    public void syncHealth(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        syncHealth(player);
    }

    public void syncHealth(Player player) {
        int hearts = getHearts(player.getUniqueId());
        double health = hearts * 2.0;

        if (player.getAttribute(Attribute.MAX_HEALTH) == null) return;

        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(health);

        // prevent player being above max hp
        if (player.getHealth() > health) {
            player.setHealth(health);
        }
    }

    private int clamp(int hearts) {
        if (hearts < minHearts) return minHearts;
        if (hearts > maxHearts) return maxHearts;
        return hearts;
    }
}
