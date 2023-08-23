package net.primegames.supremeprison.mines.api.events;

import net.primegames.supremeprison.api.events.SupremePrisonEvent;
import net.primegames.supremeprison.mines.model.mine.Mine;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public final class MineDeleteEvent extends SupremePrisonEvent implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Mine mine;
    @Getter
    @Setter
    private boolean cancelled;

    /**
     * Called when mine is deleted
     *
     * @param mine MIne
     */
    public MineDeleteEvent(Mine mine) {
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
