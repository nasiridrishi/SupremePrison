package net.primegames.supremeprison.ranks.listener;

import net.primegames.supremeprison.ranks.SupremePrisonRanks;
import me.lucko.helper.Events;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;

public class RanksListener {

    private final SupremePrisonRanks plugin;

    public RanksListener(SupremePrisonRanks plugin) {
        this.plugin = plugin;
    }

    public void register() {
        this.subscribePlayerJoinEvent();
        this.subscribePlayerQuitEvent();
    }

    private void subscribePlayerQuitEvent() {
        Events.subscribe(PlayerQuitEvent.class)
                .handler(e -> this.plugin.getRanksManager().savePlayerRank(e.getPlayer())).bindWith(plugin.getCore());
    }

    private void subscribePlayerJoinEvent() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(e -> this.plugin.getRanksManager().loadPlayerRank(Collections.singleton(e.getPlayer()))).bindWith(plugin.getCore());
    }
}
