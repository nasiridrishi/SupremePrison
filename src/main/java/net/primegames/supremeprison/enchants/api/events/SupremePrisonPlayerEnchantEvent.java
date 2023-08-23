package net.primegames.supremeprison.enchants.api.events;

import net.primegames.supremeprison.api.events.player.SupremePrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

@Getter
public final class SupremePrisonPlayerEnchantEvent extends SupremePrisonPlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final int level;
    @Getter
    @Setter
    private long tokenCost;
    @Getter
    @Setter
    private boolean cancelled;


    /**
     * Called when player enchants a tool
     *
     * @param player    Player
     * @param tokenCost cost of enchant in tokens
     * @param level     level of enchant
     */
    public SupremePrisonPlayerEnchantEvent(Player player, long tokenCost, int level) {
        super(player);
        this.tokenCost = tokenCost;
        this.level = level;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
