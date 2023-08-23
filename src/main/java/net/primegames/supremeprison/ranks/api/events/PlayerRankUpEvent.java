package net.primegames.supremeprison.ranks.api.events;

import net.primegames.supremeprison.api.events.player.SupremePrisonPlayerEvent;
import net.primegames.supremeprison.ranks.model.Rank;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

@Getter
public final class PlayerRankUpEvent extends SupremePrisonPlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Rank oldRank;

    @Getter
    @Setter
    private Rank newRank;

    @Getter
    @Setter
    private boolean cancelled;

    /**
     * Called when player receive gems
     *
     * @param player Player
     * @param oldR   old rank
     * @param newR   new rank
     */
    public PlayerRankUpEvent(Player player, Rank oldR, Rank newR) {
        super(player);
        this.oldRank = oldR;
        this.newRank = newR;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
