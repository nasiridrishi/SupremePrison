package net.primegames.supremeprison.history.api;

import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.history.model.HistoryLine;
import org.bukkit.OfflinePlayer;

import java.util.List;

public interface SupremePrisonHistoryAPI {

    /**
     * Gets players history
     *
     * @param player Player
     * @return List containing all HistoryLine.class of Player
     */
    List<HistoryLine> getPlayerHistory(OfflinePlayer player);

    /**
     * Creates a new history line for player
     *
     * @param player  Player
     * @param context Context of the history
     * @param module  SupremePrisonModule associated with the history
     */
    void createHistoryLine(OfflinePlayer player, SupremePrisonModule module, String context);
}
