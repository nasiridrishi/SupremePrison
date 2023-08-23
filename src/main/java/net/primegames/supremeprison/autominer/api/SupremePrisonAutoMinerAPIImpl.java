package net.primegames.supremeprison.autominer.api;

import net.primegames.supremeprison.autominer.SupremePrisonAutoMiner;
import org.bukkit.entity.Player;

public final class SupremePrisonAutoMinerAPIImpl implements SupremePrisonAutoMinerAPI {

    private final SupremePrisonAutoMiner plugin;

    public SupremePrisonAutoMinerAPIImpl(SupremePrisonAutoMiner plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isInAutoMinerRegion(Player player) {
        return this.plugin.getManager().isInAutoMinerRegion(player);
    }

    @Override
    public int getAutoMinerTime(Player player) {
        return this.plugin.getManager().getAutoMinerTime(player);
    }
}
