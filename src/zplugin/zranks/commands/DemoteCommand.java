package zplugin.zranks.commands;

import zplugin.zranks.api.PlayerData;
import zplugin.zranks.config.Config;
import zplugin.zranks.config.ConfigManager;
import zplugin.zranks.zRanks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class DemoteCommand implements CommandExecutor {

    zRanks plugin;
    ConfigManager manager;

    public DemoteCommand(zRanks plugin) {
        this.plugin = plugin;
        manager = new ConfigManager(plugin);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("demote")) {
            if (args.length == 1) {
                if (sender instanceof ConsoleCommandSender) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage("§6That player is not online!");
                        return true;
                    } else {
                        Config ranks = manager.getNewConfig("ranks.yml");
                        String rank = plugin.m.getRank(target);
                        String demotion = ranks.getString("ranks." + rank + ".demotion");
                        if (demotion != null) {
                            PlayerData playerData = plugin.getDatabase().find(PlayerData.class)
                                    .where().ieq("uniqueID", target.getUniqueId().toString()).findUnique();
                            playerData.setRank(demotion);
                            plugin.getDatabase().save(playerData);
                            sender.sendMessage("§6You have demoted §2" + target.getName());
                            target.sendMessage("§6You have been demoted by §4" + sender.getName());
                        } else {
                            sender.sendMessage(target.getName() + " §6can not be demoted from that rank!");
                        }
                        return true;
                    }
                } else {
                    Player player = (Player) sender;
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage("§4That player is not online!");
                        return true;
                    } else {
                        Config ranks = manager.getNewConfig("ranks.yml");
                        String rank = plugin.m.getRank(target);
                        String demotion = ranks.getString("ranks." + rank + ".demotion");
                        if (demotion != null) {
                            PlayerData playerData = plugin.getDatabase().find(PlayerData.class)
                                    .where().ieq("uniqueID", target.getUniqueId().toString()).findUnique();
                            playerData.setRank(demotion);
                            plugin.getDatabase().save(playerData);
                            sender.sendMessage("§6You have demoted §2" + target.getName());
                            target.sendMessage("§6You have been demoted by §4" + sender.getName());
                        } else {
                            sender.sendMessage(target.getName() + " §6can not be demoted from that rank!");
                        }
                        return true;
                    }
                }
            } else {
                sender.sendMessage("§4Invalid Arguments!");
            }
        }
        return false;
    }

}
