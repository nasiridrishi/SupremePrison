package net.primegames.supremeprison.tokens.api.events;

import net.primegames.supremeprison.api.enums.LostCause;
import net.primegames.supremeprison.api.events.player.SupremePrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public final class PlayerTokensLostEvent extends SupremePrisonPlayerEvent {


    private static final HandlerList handlers = new HandlerList();

    private final LostCause cause;

    @Getter
    @Setter
    private long amount;

    /**
     * Called when player loses tokens
     *
     * @param cause  LostCause
     * @param player Player
     * @param amount Amount of tokens lost
     */
    public PlayerTokensLostEvent(LostCause cause, OfflinePlayer player, long amount) {
        super(player);
        this.cause = cause;
        this.amount = amount;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
