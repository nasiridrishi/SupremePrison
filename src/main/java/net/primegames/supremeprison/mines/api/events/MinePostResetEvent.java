package net.primegames.supremeprison.mines.api.events;

import net.primegames.supremeprison.api.events.SupremePrisonEvent;
import net.primegames.supremeprison.mines.model.mine.Mine;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public final class MinePostResetEvent extends SupremePrisonEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final Mine mine;

    /**
     * Fired when mine reset was completed
     *
     * @param mine Mine
     */
    public MinePostResetEvent(Mine mine) {
        this.mine = mine;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
