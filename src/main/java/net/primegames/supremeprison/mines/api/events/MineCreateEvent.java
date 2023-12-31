package net.primegames.supremeprison.mines.api.events;

import net.primegames.supremeprison.api.events.SupremePrisonEvent;
import net.primegames.supremeprison.mines.model.mine.Mine;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public final class MineCreateEvent extends SupremePrisonEvent implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final CommandSender creator;
    @Getter
    private final Mine mine;
    @Getter
    @Setter
    private boolean cancelled;

    /**
     * Fired when mine is created
     *
     * @param creator CommandSender who created the mine
     * @param mine    Mine
     */
    public MineCreateEvent(Player creator, Mine mine) {
        this.creator = creator;
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
