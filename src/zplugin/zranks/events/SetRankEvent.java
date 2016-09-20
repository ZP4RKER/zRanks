package zplugin.zranks.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SetRankEvent extends Event {

    private HandlerList handlerList = new HandlerList();

    public HandlerList getHandlers() {
        return handlerList;
    }

}
