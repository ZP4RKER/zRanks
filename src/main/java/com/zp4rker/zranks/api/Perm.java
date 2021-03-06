package com.zp4rker.zranks.api;

import com.zp4rker.zranks.config.ConfigManager;
import com.zp4rker.zranks.config.Config;
import com.zp4rker.zranks.zRanks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;

public class Perm {

    private static zRanks plugin;
    private static ConfigManager manager;
    private static Config ranks;

    public Perm(zRanks plugin) {
        Perm.plugin = plugin;
        manager = new ConfigManager(plugin);
    }

    public static HashMap<Player, PermissionAttachment> attachments = new HashMap<>();

    public static void resetPerms() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PermissionAttachment attachment = attachments.get(player);
            if (attachment != null) {
                player.removeAttachment(attachment);
                attachments.remove(player);
            }
        }
    }

    public void reloadPerms() {

        resetPerms();

        for (Player player : Bukkit.getOnlinePlayers()) {

            ranks = manager.getNewConfig("ranks.yml");

            String rank = plugin.m.getRank(player);

            PermissionAttachment attachment = player.addAttachment(plugin);
            attachments.put(player, attachment);

            if (ranks.getList("ranks." + rank + ".perms") != null) {

                for (Object Perm : ranks.getList("ranks." + rank + ".perms")) {

                    String perm = Perm.toString();
                    attachment.setPermission(perm, true);

                }

                player.setPlayerListName(plugin.m.getFullPrefix(player) + " §r" + player.getDisplayName());

            }

        }

    }

}