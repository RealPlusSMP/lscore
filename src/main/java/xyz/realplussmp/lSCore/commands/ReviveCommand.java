package xyz.realplussmp.lSCore.commands;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import xyz.realplussmp.lSCore.manager.ConfigManager;
import xyz.realplussmp.lSCore.manager.DataManager;
import xyz.realplussmp.lSCore.manager.HeartManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReviveCommand implements CommandExecutor, TabCompleter {

    private final HeartManager heartManager;
    private final DataManager dataManager;
    private final ConfigManager config;

    public ReviveCommand(HeartManager heartManager, DataManager dataManager, ConfigManager config) {
        this.heartManager = heartManager;
        this.dataManager = dataManager;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("/revive <player>");
            return true;
        }

        String targetName = args[0];

        // unban
        Bukkit.getBanList(BanList.Type.NAME).pardon(targetName);

        Player target = Bukkit.getPlayerExact(targetName);

        if (target != null) {
            UUID uuid = target.getUniqueId();

            heartManager.setHearts(uuid, config.getDefaultHearts());
            dataManager.saveHearts(uuid, config.getDefaultHearts());

            heartManager.syncHealth(target);

            return true;
        }

        sender.sendMessage("you revived " + target.getName());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> names = new ArrayList<>();
            for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                names.add(p.getName());
            }
            return names;
        }
        return List.of();
    }
}
