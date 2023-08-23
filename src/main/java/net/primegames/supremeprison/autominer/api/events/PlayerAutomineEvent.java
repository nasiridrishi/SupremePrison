package net.primegames.supremeprison.autominer.api.events;

import net.primegames.supremeprison.api.events.player.SupremePrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public final class PlayerAutomineEvent extends SupremePrisonPlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final int timeLeft;
    @Getter
    @Setter
    private boolean cancelled;

    /**
     * Called when player auto mines in region
     *
     * @param player   Player
     * @param timeLeft Timeleft in seconds of player's autominer time
     */
    public PlayerAutomineEvent(Player player, int timeLeft) {
        super(player);
        this.timeLeft = timeLeft;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
