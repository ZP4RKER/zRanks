package zplugin.zranks.api;

import zplugin.zranks.zRanks;

public class PHAPI {

    private zRanks plugin;

    public PHAPI(zRanks plugin) {
        this.plugin = plugin;
    }

    public void enablePlaceHolders() {
        be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(plugin, "staffcount",
                new be.maximvdw.placeholderapi.PlaceholderReplacer() {
                    @Override
                    public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent event) {
                        return "" + plugin.m.getStaffList().size();
                    }
                });

        be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(plugin, "rank",
                new be.maximvdw.placeholderapi.PlaceholderReplacer() {
                    @Override
                    public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent event) {
                        return plugin.m.getRank(event.getPlayer());
                    }
                });
    }

}
