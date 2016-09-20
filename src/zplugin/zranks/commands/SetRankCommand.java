package zplugin.zranks.commands;

import zplugin.zranks.api.Perm;
import zplugin.zranks.api.PlayerData;
import zplugin.zranks.config.Config;
import zplugin.zranks.config.ConfigManager;
import zplugin.zranks.zRanks;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class SetRankCommand implements CommandExecutor {

    zRanks plugin;
    ConfigManager manager;
    Config ranks;

    public SetRankCommand(zRanks plugin) {
        this.plugin = plugin;
        manager = new ConfigManager(plugin);
        ranks = manager.getNewConfig("ranks.yml");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setrank")) {
            if (sender instanceof ConsoleCommandSender) {
                if (args.length == 2) {
                    String playerName = args[0];
                    String rank = args[1];
                    Player player = Bukkit.getPlayer(playerName);
                    if (player != null) {
                        if (ranks.get("ranks." + rank) != null) {
                            PlayerData playerData = plugin.getDatabase().find(PlayerData.class)
                                    .where().ieq("uniqueID", player.getUniqueId().toString()).findUnique();
                            playerData.setRank(rank);
                            plugin.getDatabase().save(playerData);
                            sender.sendMessage("§6Added §r" + player.getDisplayName() + " §6to the rank §2" + rank);
                            player.sendMessage("§6You were added to the rank §2" + rank);
                            Perm.reloadPerms();
                            return true;
                        } else {
                            sender.sendMessage("§4That rank does not exist!");
                            return true;
                        }
                    } else {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                        if (offlinePlayer != null) {
                            if (ranks.get("ranks." + rank) != null) {
                                PlayerData playerData = plugin.getDatabase().find(PlayerData.class)
                                        .where().ieq("uniqueID", offlinePlayer.getUniqueId().toString()).findUnique();
                                playerData.setRank(rank);
                                plugin.getDatabase().save(playerData);
                                sender.sendMessage("§6Added §r" + offlinePlayer.getName() + " §6to the rank §2" + rank);
                                Perm.reloadPerms();
                                return true;
                            } else {
                                sender.sendMessage("§4That rank does not exist!");
                                return true;
                            }
                        }
                    }
                } else {
                    sender.sendMessage("§4Invalid Arguments!");
                    return false;
                }
            } else if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 2) {
                    String playerName = args[0];
                    String rank = args[1];
                    Player target = Bukkit.getPlayer(playerName);
                    if (target != null) {
                        if (ranks.get("ranks." + rank) != null) {
                            PlayerData playerData = plugin.getDatabase().find(PlayerData.class)
                                    .where().ieq("uniqueID", player.getUniqueId().toString()).findUnique();
                            playerData.setRank(rank);
                            plugin.getDatabase().save(playerData);
                            if (player == target) {
                                target.sendMessage("§6You were added to the rank §2" + rank);
                            } else {
                                sender.sendMessage("§6Added §r" + target.getDisplayName() + " §6to the rank §2" + rank);
                                target.sendMessage("§6You were added to the rank §2" + rank);
                            }
                            Perm.reloadPerms();
                            return true;
                        } else {
                            sender.sendMessage("§4That rank does not exist!");
                            return true;
                        }
                    } else {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                        if (offlinePlayer != null) {
                            if (ranks.get("ranks." + rank) != null) {
                                PlayerData playerData = plugin.getDatabase().find(PlayerData.class)
                                        .where().ieq("uniqueID", offlinePlayer.getUniqueId().toString()).findUnique();
                                playerData.setRank(rank);
                                plugin.getDatabase().save(playerData);
                                sender.sendMessage("§6Added §r" + offlinePlayer.getName() + " §6to the rank §2" + rank);
                                Perm.reloadPerms();
                                return true;
                            } else {
                                sender.sendMessage("§4That rank does not exist!");
                                return true;
                            }
                        }
                    }
                } else {
                    player.sendMessage("§4Invalid Arguments!");
                    return false;
                }
            }
        }
        return false;
    }

}
