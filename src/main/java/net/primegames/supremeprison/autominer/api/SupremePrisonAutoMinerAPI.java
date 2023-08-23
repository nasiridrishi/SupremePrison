package net.primegames.supremeprison.autominer.api;

import org.bukkit.entity.Player;

public interface SupremePrisonAutoMinerAPI {

    /**
     * Returns true if player is in autominer region, otherwise return false
     *
     * @param player Player
     * @return returns true if player is in autominer region, otherwise return false
     */
    boolean isInAutoMinerRegion(Player player);

    /**
     * Returns remaining autominer time in seconds for specific player
     *
     * @param player Player
     * @return time in seconds left for specific player autominer
     */
    int getAutoMinerTime(Player player);
}
