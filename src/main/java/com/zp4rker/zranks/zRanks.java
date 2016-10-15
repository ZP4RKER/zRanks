package com.zp4rker.zranks;

import com.zp4rker.zranks.api.*;
import com.zp4rker.zranks.commands.*;
import com.zp4rker.zranks.config.ConfigManager;
import com.zp4rker.zranks.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import com.zp4rker.zranks.config.Config;
import com.zp4rker.zranks.listeners.PlayerChatListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class zRanks extends JavaPlugin {

    public Methods m;
    private boolean isNewVersion = false;
    private int checkVersions;

    public void onEnable() {

        // Configs
        ConfigManager manager = new ConfigManager(this);
        Config ranks;

        // Instance of Methods class
        m = new Methods(this);

        // Enabled Message
        getLogger().info("zRanks enabled!");

        // Reload Perms
        new Perm(this).reloadPerms();
        // Colour all name tags
        m.colourAllNames();

        // Register Events
        getServer().getPluginManager().registerEvents(new PlayerChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        // Set Command Executors
        getCommand("zranks").setExecutor(new AdminCommands(this));
        getCommand("setrank").setExecutor(new SetRankCommand(this));
        getCommand("who").setExecutor(new WhoCommand(this));
        getCommand("staff").setExecutor(new StaffCommand(this));
        getCommand("promote").setExecutor(new PromoteCommand(this));
        getCommand("demote").setExecutor(new DemoteCommand(this));

        // Load Default Config
        ranks = manager.getNewConfig("ranks.yml");
        if (ranks.get("ranks") == null) {
            ranks.saveDefaultConfig();
        }
        saveDefaultConfig();

        // Setup Databse
        setupDatabase();

        // Check for Updates
        checkVersions = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if (!isNewVersion) {
                    String version = getDescription().getVersion();
                    if (m.getLatestVersion() != version) {
                        if (Double.parseDouble(version) < Double.parseDouble(m.getLatestVersion())) {
                            isNewVersion = true;
                        }
                    }
                } else {
                    getLogger().info("A new version of zRanks is available!");
                    cancelEvent(checkVersions);
                }
            }
        }, 0, 432000);
        if (!isNewVersion) {
            getLogger().info("You have the latest version of zRanks!");
        }

        // Check if timedRanks is enabled
        if (getConfig().getBoolean("timedRanks-enabled")) {
            // Start the task
            new TimedRanks(this);
        }

    }

    public void cancelEvent(int i) {
        Bukkit.getServer().getScheduler().cancelTask(i);
    }

    public void onDisable() {

        // Disabled Message
        getLogger().info("zRanks disabled!");
        // Reset all permissions
        Perm.resetPerms();

    }

    public void setupDatabase() {
        try {
            getDatabase().find(PlayerData.class).findRowCount();
        } catch (Exception e) {
            getLogger().info("Installing database due to first time usage...");
            installDDL();
            getLogger().info("Database installed.");
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<>();
        list.add(PlayerData.class);
        return list;
    }

}
