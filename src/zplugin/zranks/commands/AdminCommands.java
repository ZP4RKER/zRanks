package zplugin.zranks.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import zplugin.zranks.zRanks;

public class AdminCommands implements CommandExecutor {

    zRanks plugin;

    public AdminCommands(zRanks plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("zranks")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.getPluginLoader().disablePlugin(plugin);
                    plugin.getPluginLoader().enablePlugin(plugin);
                    sender.sendMessage("§2zRanks reloaded!");
                    return true;
                } else if (args[0].equalsIgnoreCase("update")) {
                    plugin.m.update(sender);
                    return true;
                }
            } else {
                sender.sendMessage("§4Invalid Arguments!");
            }
        }
        return false;
    }
}
