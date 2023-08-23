package net.primegames.supremeprison.api.events.player;

import net.primegames.supremeprison.api.events.SupremePrisonEvent;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

@Getter
public abstract class SupremePrisonPlayerEvent extends SupremePrisonEvent {

    protected OfflinePlayer player;

    /**
     * Abstract SupremePrisonPlayerEvent
     *
     * @param player Player
     */
    public SupremePrisonPlayerEvent(OfflinePlayer player) {
        this.player = player;
    }
}
