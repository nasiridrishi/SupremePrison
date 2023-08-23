package net.primegames.supremeprison.multipliers.api.events;

import net.primegames.supremeprison.api.events.player.SupremePrisonPlayerEvent;
import net.primegames.supremeprison.multipliers.enums.MultiplierType;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@Getter
public final class PlayerMultiplierReceiveEvent extends SupremePrisonPlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final double multiplier;

    @Getter
    private final TimeUnit timeUnit;

    @Getter
    private final long duration;

    @Getter
    private final MultiplierType type;

    /**
     * Called when player receive multiplier
     *
     * @param player     Player
     * @param multiplier multiplier
     * @param duration   duration
     * @param timeUnit   TimeUnit
     */
    public PlayerMultiplierReceiveEvent(Player player, double multiplier, TimeUnit timeUnit, long duration, MultiplierType type) {
        super(player);
        this.multiplier = multiplier;
        this.timeUnit = timeUnit;
        this.duration = duration;
        this.type = type;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
