package com.zp4rker.zranks.listeners;

import com.zp4rker.zranks.config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.zp4rker.zranks.api.PlayerData;
import com.zp4rker.zranks.config.Config;
import com.zp4rker.zranks.zRanks;


@SuppressWarnings("unused")
public class PlayerJoinListener implements Listener {

    zRanks plugin;
    Config ranks;

    public PlayerJoinListener(zRanks plugin) {
        this.plugin = plugin;
        ranks = new ConfigManager(plugin).getNewConfig("ranks.yml");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        player.setPlayerListName(plugin.m.getFullPrefix(player) + " §r" + player.getDisplayName());

        if (plugin.m.getRank(player) == null) {

            if (plugin.m.getDefaultRank() == null) {

                plugin.getLogger().warning("NO DEFAULT RANK INCLUDED IN RANKS.YML!");

            } else {

                PlayerData playerData = new PlayerData();
                playerData.setUniqueID(player.getUniqueId().toString());
                playerData.setRank(plugin.m.getDefaultRank());
                plugin.getDatabase().save(playerData);

            }

        }

        plugin.m.colourName(player);

    }

}
