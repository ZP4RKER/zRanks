package zplugin.zranks.commands;

import zplugin.zranks.config.ConfigManager;
import zplugin.zranks.zRanks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("unused")
public class StaffCommand implements CommandExecutor {

    zRanks plugin;
    ConfigManager manager;

    public StaffCommand(zRanks plugin) {
        this.plugin = plugin;
        manager = new ConfigManager(plugin);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("staff")) {
            if (sender instanceof ConsoleCommandSender) {
                if (plugin.getServer().getOnlinePlayers().size() == 0) {
                    sender.sendMessage("§6No players are online!");
                    return true;
                } else {
                    StringBuilder message = new StringBuilder();
                    message.append("§6Staff: ");
                    List<String> players = plugin.m.getStaffList();
                    for (int i = 0; i < players.size(); i++) {
                        if (i + 1 == players.size()) {
                            message.append(players.get(i) + "§6.");
                        } else {
                            message.append(players.get(i) + "§6, ");
                        }
                    }
                    return true;
                }
            } else if (sender instanceof Player) {
                Player player = (Player) sender;
                if (plugin.getServer().getOnlinePlayers().size() == 1) {
                    player.sendMessage("§6You are the only online player!");
                    return true;
                } else {
                    StringBuilder message = new StringBuilder();
                    message.append("§6Staff: ");
                    List<String> players = plugin.m.getStaffList();
                    for (int i = 0; i < players.size(); i++) {
                        if (i + 1 == players.size()) {
                            message.append(players.get(i) + "§6.");
                        } else {
                            message.append(players.get(i) + "§6, ");
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
