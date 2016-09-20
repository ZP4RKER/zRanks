package zplugin.zranks.api;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import zplugin.zranks.config.Config;
import zplugin.zranks.config.ConfigManager;
import zplugin.zranks.zRanks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Methods {

    zRanks plugin;
    ConfigManager manager;
    Config ranks;
    Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    public Methods(zRanks plugin) {
        this.plugin = plugin;
        manager = new ConfigManager(plugin);
        ranks = manager.getNewConfig("ranks.yml");
    }

    public String getRank(Player player) {
        PlayerData playerData = plugin.getDatabase().find(PlayerData.class)
                .where().ieq("uniqueID", player.getUniqueId().toString()).findUnique();
        return playerData != null ? playerData.getRank() : null;
    }

    public List<String> getStaffList() {
        ranks.reloadConfig();
        List<String> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            String rank = getRank(player);
            if (rank != null) {
                boolean staff = ranks.getBoolean("ranks." + rank + ".staff");
                if (staff) {
                    players.add(getFullDisplayName(player));
                }
            }
        }
        return players;
    }

    public List<String> getPlayerList() {
        List<String> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(getFullDisplayName(player));
        }
        return players;
    }

    public String getFullPrefix(Player player) {
        String prefix = getPrefix(player);
        String fullPrefix = plugin.getConfig().getString("prefixFormat").replace("%prefix%", prefix);
        return fullPrefix;
    }

    public String getPrefix(Player player) {
        ranks.reloadConfig();
        String rank = getRank(player);
        String prefix = "";
        if (rank != null) {
            PlayerData playerData = plugin.getDatabase().find(PlayerData.class)
                    .where().ieq("uniqueID", player.getUniqueId().toString()).findUnique();
            if (playerData.getPrefix() != null) {
                prefix = playerData.getPrefix();
            } else {
                if (rank != null) {
                    prefix = ranks.getString("ranks." + rank + ".prefix");
                }
            }
        }
        return prefix;
    }

    public String getDefaultRank() {
        ranks.reloadConfig();
        String rank = null;
        for (Object Rank : ranks.getConfigurationSection("ranks").getKeys(false)) {
            boolean isDefault = ranks.getBoolean("ranks." + Rank.toString() + ".default");
            if (isDefault) {
                rank = Rank.toString();
            }
        }
        return rank;
    }

    public String getFullDisplayName(Player player) {
        String prefix = getFullPrefix(player);
        String displayName = player.getDisplayName();
        return prefix + displayName;
    }

    public String getJSON(String url) {
        HttpURLConnection c = null;
        try {

            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.connect();
            int response = c.getResponseCode();

            switch (response) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (Exception e) {
        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        return null;
    }

    public String getLatestVersion() {
        String data = getJSON("http://api.bukget.org/3/plugins/bukkit/zranks");
        JSONObject jsonObject = null;
        if (data != null) {
            JSONParser parser = new JSONParser();
            try {
                jsonObject = (JSONObject) parser.parse(data);
            } catch (Exception e) {
            }
        }
        JSONArray versions;
        JSONObject versionObj;
        String version = null;
        if (jsonObject.get("versions") instanceof JSONArray) {
            versions = (JSONArray) jsonObject.get("versions");
            for (int i = 0; i < versions.size(); i++) {
                if (version != null) {
                    if (((JSONArray) ((JSONObject) versions.get(i)).get("game_versions")).get(0).toString().contains(getMCVersion())) {
                        if (Double.parseDouble(version) < Double.parseDouble(((JSONObject) versions.get(i)).get("version").toString())) {
                            version = ((JSONObject) versions.get(i)).get("version").toString();
                        }
                    }
                } else {
                    if (((JSONArray) ((JSONObject) versions.get(i)).get("game_versions")).get(0).toString().contains(getMCVersion())) {
                        version = ((JSONObject) versions.get(i)).get("version").toString();
                    }
                }
            }
        }
        return version;
    }

    public void colourName(Player player) {
        String rank = getRank(player);
        String color = getNameTagColour(player);
        Team team;
        if (!Variables.isRegistered.contains(rank)) {
            team = scoreboard.registerNewTeam(rank);
            Variables.isRegistered.add(rank);
        } else {
            team = scoreboard.getTeam(rank);
        }
        team.setPrefix(color);
        team.addPlayer(player);
        player.setScoreboard(scoreboard);
    }

    public void colourAllNames() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            colourName(player);
        }
    }

    public String getNameTagColour(Player player) {
        ranks.reloadConfig();
        String name = ranks.getString("ranks." + getRank(player) + ".nameTag-color", null);
        if (name != null) {
            switch (name) {
                case "AQUA":
                    name = "§" + ChatColor.AQUA.getChar();
                case "BLACK":
                    name = "§" + ChatColor.BLACK.getChar();
                case "BLUE":
                    name = "§" + ChatColor.BLUE.getChar();
                case "DARK_AQUA":
                    name = "§" + ChatColor.DARK_AQUA.getChar();
                case "DARK_BLUE":
                    name = "§" + ChatColor.DARK_BLUE.getChar();
                case "DARK_GRAY":
                    name = "§" + ChatColor.DARK_GRAY.getChar();
                case "DARK_GREEN":
                    name = "§" + ChatColor.DARK_GREEN.getChar();
                case "DARK_PURPLE":
                    name = "§" + ChatColor.DARK_PURPLE.getChar();
                case "DARK_RED":
                    name = "§" + ChatColor.DARK_RED.getChar();
                case "GOLD":
                    name = "§" + ChatColor.GOLD.getChar();
                case "GREEN":
                    name = "§" + ChatColor.GREEN.getChar();
                case "GRAY":
                    name = "§" + ChatColor.GRAY.getChar();
                case "LIGHT_PURPLE":
                    name = "§" + ChatColor.LIGHT_PURPLE.getChar();
                case "RED":
                    name = "§" + ChatColor.RED.getChar();
                case "WHITE":
                    name = "§" + ChatColor.WHITE.getChar();
                case "YELLOW":
                    name = "§" + ChatColor.YELLOW.getChar();
            }
        } else {
            name = "§r";
        }
        return name;
    }

    public int getTicksFromRank(String rank) {
        int ticks = 0;
        if (ranks.get("ranks." + rank) != null) {
            if (ranks.getString("ranks." + rank + ".timeToPromote") != null) {
                String time = ranks.getString("ranks." + rank + ".timeToPromote");
                String[] units = null;
                if (time.contains(" ")) {
                    units = time.split(" ");
                    for (String unit : units) {
                        if (unit.contains("h")) {
                            unit.replace("h", "");
                            try {
                                ticks += ((Integer.parseInt(unit) * 20) * 60) * 60;
                            } catch (Exception e) {}
                        }
                        if (unit.contains("m")) {
                            unit.replace("m", "");
                            try {
                                ticks += (Integer.parseInt(unit) * 20) * 60;
                            } catch (Exception e) {}
                        }
                        if (unit.contains("s")) {
                            unit.replace("s", "");
                            try {
                                ticks += Integer.parseInt(unit) * 20;
                            } catch (Exception e) {}
                        }
                    }
                }
            }
        }
        return ticks;
    }

    public String getDownload() {
        String data = getJSON("http://api.bukget.org/3/plugins/bukkit/zranks/latest");
        JSONObject jsonObject = null;
        if (data != null) {
            JSONParser parser = new JSONParser();
            try {
                jsonObject = (JSONObject) parser.parse(data);
            } catch (Exception e) {
            }
        }
        JSONArray versions;
        JSONObject versionObj;
        String download = null;
        if (jsonObject.get("versions") instanceof JSONArray) {
            versions = (JSONArray) jsonObject.get("versions");
            versionObj = (JSONObject) versions.get(0);
            download = versionObj.get("download").toString();
        }
        return download;
    }

    public String getDownload(String versionName) {

        String data = getJSON("http://api.bukget.org/3/plugins/bukkit/zranks");
        JSONObject jsonObject = null;
        if (data != null) {
            JSONParser parser = new JSONParser();
            try {
                jsonObject = (JSONObject) parser.parse(data);
            } catch (Exception e) {
            }
        }

        JSONArray versions = (JSONArray) jsonObject.get("versions");
        List<JSONObject> validVersions = new ArrayList<>();

        for (int i = 0; i < versions.size(); i++) {

            JSONObject version = (JSONObject) versions.get(i);

            if (((JSONArray) version.get("game_versions")).get(0).toString().contains(getMCVersion())) {
                validVersions.add(version);
            }

        }

        JSONObject latest = null;

        for (JSONObject version : validVersions) {

            long currentVersion = 0;

            if (latest == null) {
                latest = version;
                currentVersion = Long.parseLong(version.get("version").toString());
            }

            if (Long.parseLong(version.get("version").toString()) > currentVersion) {
                currentVersion = Long.parseLong(version.get("version").toString());
                latest = version;
            }

        }

        return latest.get("download").toString();

    }

    public void update(CommandSender sender) {
        String updateURL = getDownload();
        String out;
        try
        {
            File to = new File(plugin.getServer().getUpdateFolderFile(), updateURL.substring(updateURL.lastIndexOf('/') + 1, updateURL.length()));
            File tmp = new File(to.getPath()+".au");
            if(!tmp.exists())
            {
                plugin.getServer().getUpdateFolderFile().mkdirs();
                tmp.createNewFile();
            }
            URL url = new URL(updateURL);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(tmp);
            byte[] buffer = new byte[4096];
            int fetched;
            while((fetched = is.read(buffer)) != -1)
                os.write(buffer, 0, fetched);
            is.close();
            os.flush();
            os.close();
            if(to.exists())
                to.delete();
            tmp.renameTo(to);
            sender.sendMessage("§2Restart server to update!");
        }
        catch(Exception e)
        {
            sender.sendMessage("§4Failed to update!");
        }
    }

    public String getMCVersion() {
        return plugin.getServer().getBukkitVersion().substring(0, 3);
    }

}