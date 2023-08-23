package net.primegames.supremeprison.autominer.listener;

import net.primegames.supremeprison.autominer.SupremePrisonAutoMiner;
import me.lucko.helper.Events;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AutoMinerListener {

    private final SupremePrisonAutoMiner plugin;

    public AutoMinerListener(SupremePrisonAutoMiner plugin) {
        this.plugin = plugin;
    }

    public void subscribeToEvents() {
        this.subscribeToPlayerJoinEvent();
        this.subscribeToPlayerQuitEvent();
    }

    private void subscribeToPlayerQuitEvent() {
        Events.subscribe(PlayerQuitEvent.class)
                .handler(e -> this.plugin.getManager().savePlayerAutoMinerData(e.getPlayer(), true)).bindWith(this.plugin.getCore());
    }

    private void subscribeToPlayerJoinEvent() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(e -> this.plugin.getManager().loadPlayerAutoMinerData(e.getPlayer())).bindWith(this.plugin.getCore());
    }

}
