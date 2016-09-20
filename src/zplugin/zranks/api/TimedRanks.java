package zplugin.zranks.api;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import zplugin.zranks.config.Config;
import zplugin.zranks.config.ConfigManager;
import zplugin.zranks.zRanks;

public class TimedRanks {

    private static int timerTask;

    public TimedRanks(zRanks plugin) {

        ConfigManager manager = new ConfigManager(plugin);
        Config ranks = manager.getNewConfig("ranks.yml");

        timerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {

                    PlayerData playerData = plugin.getDatabase().find(PlayerData.class)
                            .where().ieq("uniqueID", player.getUniqueId().toString()).findUnique();
                    playerData.setTicks(playerData.getTicks() + 1);
                    plugin.getDatabase().save(playerData);

                    int ticks = playerData.getTicks();
                    if (plugin.m.getTicksFromRank(playerData.getRank()) != 0) {
                        if (ticks == plugin.m.getTicksFromRank(playerData.getRank())) {
                            String promotion = ranks.getString("ranks." + playerData.getRank() + ".promotion");
                            if (promotion != null) {
                                playerData.setRank(promotion);
                                plugin.getDatabase().save(playerData);
                            }
                            else {
                                plugin.getLogger().warning("NO PROMOTION SET FOR RANK: " + playerData.getRank() + "!");
                            }
                        }
                    }

                }
            }
        }, 0, 1);

    }

    public static void cancelTask() {
        Bukkit.getScheduler().cancelTask(timerTask);
    }

}
