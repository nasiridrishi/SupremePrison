package net.primegames.supremeprison.prestiges.api.events;

import net.primegames.supremeprison.api.events.player.SupremePrisonPlayerEvent;
import net.primegames.supremeprison.prestiges.model.Prestige;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

@Getter
public final class PlayerPrestigeEvent extends SupremePrisonPlayerEvent implements Cancellable {


    private static final HandlerList handlers = new HandlerList();

    private final Prestige oldPrestige;

    @Getter
    @Setter
    private Prestige newPrestige;

    @Getter
    @Setter
    private boolean cancelled;

    /**
     * Called when player receive gems
     *
     * @param player      Player
     * @param oldPrestige old prestige
     * @param newPrestige new prestige
     */
    public PlayerPrestigeEvent(Player player, Prestige oldPrestige, Prestige newPrestige) {
        super(player);
        this.oldPrestige = oldPrestige;
        this.newPrestige = newPrestige;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
