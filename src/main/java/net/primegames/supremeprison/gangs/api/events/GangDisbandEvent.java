package net.primegames.supremeprison.gangs.api.events;

import net.primegames.supremeprison.api.events.SupremePrisonEvent;
import net.primegames.supremeprison.gangs.model.Gang;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

@Getter
public final class GangDisbandEvent extends SupremePrisonEvent implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Gang gang;
    private boolean cancelled;

    /**
     * Called when gang is disbanded
     *
     * @param gang Gang
     */
    public GangDisbandEvent(Gang gang) {
        this.gang = gang;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
