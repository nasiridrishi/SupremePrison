package net.primegames.supremeprison.prestiges.listener;

import net.primegames.supremeprison.prestiges.SupremePrisonPrestiges;
import me.lucko.helper.Events;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PrestigeListener {
    private final SupremePrisonPrestiges plugin;

    public PrestigeListener(SupremePrisonPrestiges plugin) {
        this.plugin = plugin;
    }

    public void register() {
        this.subscribePlayerJoinEvent();
        this.subscribePlayerQuitEvent();
    }

    private void subscribePlayerQuitEvent() {
        Events.subscribe(PlayerQuitEvent.class)
                .handler(e -> this.plugin.getPrestigeManager().savePlayerData(e.getPlayer(), true, true)).bindWith(plugin.getCore());
    }

    private void subscribePlayerJoinEvent() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(e -> this.plugin.getPrestigeManager().loadPlayerPrestige(e.getPlayer())).bindWith(plugin.getCore());
    }


}
