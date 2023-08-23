package net.primegames.supremeprison.gangs.api;

import net.primegames.supremeprison.gangs.model.Gang;
import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.Optional;

public interface SupremePrisonGangsAPI {

    /**
     * Method to get Gang from player
     *
     * @param player OfflinePlayer
     * @return Optional<Gang> gang
     */
    Optional<Gang> getPlayerGang(OfflinePlayer player);

    /**
     * Method to get Gang from name
     *
     * @param name name of gang
     * @return Optional<Gang> gang
     */
    Optional<Gang> getByName(String name);

    /**
     * Method to get all gangs
     *
     * @return List of gangs
     */
    Collection<Gang> getAllGangs();


}
