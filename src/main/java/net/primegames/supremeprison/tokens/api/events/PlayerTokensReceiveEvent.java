package net.primegames.supremeprison.tokens.api.events;

import net.primegames.supremeprison.api.enums.ReceiveCause;
import net.primegames.supremeprison.api.events.player.SupremePrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

@Getter
public final class PlayerTokensReceiveEvent extends SupremePrisonPlayerEvent implements Cancellable {


    private static final HandlerList handlers = new HandlerList();

    private final ReceiveCause cause;

    @Getter
    @Setter
    private long amount;

    @Getter
    @Setter
    private boolean cancelled;

    /**
     * Called when player receive tokens
     *
     * @param cause  ReceiveCause
     * @param player Player
     * @param amount Amount of tokens received
     */
    public PlayerTokensReceiveEvent(ReceiveCause cause, OfflinePlayer player, long amount) {
        super(player);
        this.cause = cause;
        this.amount = amount;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
