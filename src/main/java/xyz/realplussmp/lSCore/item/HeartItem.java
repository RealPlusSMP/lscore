package xyz.realplussmp.lSCore.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.realplussmp.lSCore.util.MessageUtil;

import java.util.List;

public class HeartItem {

    private static NamespacedKey HEART_KEY;

    public static void init(JavaPlugin plugin) {
        HEART_KEY = new NamespacedKey(plugin, "heart_item");
    }

    public static ItemStack create(int amount) {
        ItemStack item = new ItemStack(Material.NETHER_STAR, amount);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.displayName(MessageUtil.get("<red>Heart"));
        meta.lore(List.of(
                MessageUtil.get("<gray>This is a heart, right click to interact")
        ));
        meta.getPersistentDataContainer().set(HEART_KEY, PersistentDataType.INTEGER, 1);

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isHeart(ItemStack item) {
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        Integer val = meta.getPersistentDataContainer().get(HEART_KEY, PersistentDataType.INTEGER);
        return val != null && val == 1;
    }
}
