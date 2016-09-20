package zplugin.zranks.listeners;

import zplugin.zranks.zRanks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@SuppressWarnings("unused")
public class PlayerChatListener implements Listener {

    zRanks plugin;

    public PlayerChatListener(zRanks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String prefix = plugin.m.getFullPrefix(player);
        String format = plugin.getConfig().getString("chatFormat")
                .replace("%prefix%", prefix)
                .replace("%displayName%", player.getDisplayName())
                .replace("%message%", event.getMessage());
        event.setFormat(format);
    }

}
