package com.zp4rker.zranks.commands;

import com.zp4rker.zranks.config.ConfigManager;
import com.zp4rker.zranks.zRanks;
import com.zp4rker.zranks.api.PlayerData;
import com.zp4rker.zranks.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class PromoteCommand implements CommandExecutor {

    zRanks plugin;
    ConfigManager manager;

    public PromoteCommand(zRanks plugin) {
        this.plugin = plugin;
        manager = new ConfigManager(plugin);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("promote")) {
            if (args.length == 1) {
                if (sender instanceof ConsoleCommandSender) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage("§6That player is not online!");
                        return true;
                    } else {
                        Config ranks = manager.getNewConfig("ranks.yml");
                        String rank = plugin.m.getRank(target);
                        String promotion = ranks.getString("ranks." + rank + ".promotion");
                        if (promotion != null) {
                            PlayerData playerData = plugin.getDatabase().find(PlayerData.class)
                                    .where().ieq("uniqueID", target.getUniqueId().toString()).findUnique();
                            playerData.setRank(promotion);
                            plugin.getDatabase().save(playerData);
                            sender.sendMessage("§6You have promoted §2" + target.getName());
                            target.sendMessage("§6You have been promoted by §4" + sender.getName());
                        } else {
                            sender.sendMessage(target.getName() + " §6can not be promoted from that rank!");
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
                        String rank = ranks.getString("players." + target.getUniqueId() + ".rank");
                        String promotion = ranks.getString("ranks." + rank + ".promotion");
                        if (promotion != null) {
                            PlayerData playerData = plugin.getDatabase().find(PlayerData.class)
                                    .where().ieq("uniqueID", target.getUniqueId().toString()).findUnique();
                            playerData.setRank(promotion);
                            plugin.getDatabase().save(playerData);
                            sender.sendMessage("§6You have promoted §2" + target.getName());
                            target.sendMessage("§6You have been promoted by §4" + sender.getName());
                        } else {
                            sender.sendMessage(target.getName() + " §6can not be promoted from that rank!");
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

