package zplugin.zranks.commands;

import zplugin.zranks.zRanks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("unused")
public class WhoCommand implements CommandExecutor {

    zRanks plugin;

    public WhoCommand(zRanks plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("who")) {
            if (Bukkit.getOnlinePlayers().size() == 1) {
                if (sender instanceof Player) {
                    sender.sendMessage("§6You are the only online player!");
                    return true;
                } else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        List<String> players = plugin.m.getPlayerList();
                        StringBuilder message = new StringBuilder();
                        message.append("§6Online Players: ");
                        for (int i = 0; i < players.size(); i++) {
                            if (i + 1 == players.size()) {
                                message.append(players.get(i) + "§6.");
                            } else {
                                message.append(players.get(i) + "§6, ");
                            }
                        }
                        sender.sendMessage(message.toString());
                    }
                    return true;
                }
            } else if (Bukkit.getOnlinePlayers().size() == 0) {
                sender.sendMessage("§6No players are online!");
                return true;
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    List<String> players = plugin.m.getPlayerList();
                    StringBuilder message = new StringBuilder();
                    message.append("§6Online Players: ");
                    for (int i = 0; i < players.size(); i++) {
                        if (i + 1 == players.size()) {
                            message.append(players.get(i) + "§6.");
                        } else {
                            message.append(players.get(i) + "§6, ");
                        }
                    }
                    sender.sendMessage(message.toString());
                }
                return true;
            }
        }
        return false;
    }

}
