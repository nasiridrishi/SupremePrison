package net.primegames.supremeprison.multipliers.api.events;

import net.primegames.supremeprison.api.events.player.SupremePrisonPlayerEvent;
import net.primegames.supremeprison.multipliers.multiplier.PlayerMultiplier;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Deprecated
public final class PlayerMultiplierExpireEvent extends SupremePrisonPlayerEvent {


    private static final HandlerList handlers = new HandlerList();

    private final PlayerMultiplier multiplier;

    /**
     * Called when player's multiplier expires
     *
     * @param player     Player
     * @param multiplier multiplier
     */
    public PlayerMultiplierExpireEvent(Player player, PlayerMultiplier multiplier) {
        super(player);
        this.multiplier = multiplier;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
