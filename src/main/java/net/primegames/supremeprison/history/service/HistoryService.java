package net.primegames.supremeprison.history.service;

import net.primegames.supremeprison.history.model.HistoryLine;
import org.bukkit.OfflinePlayer;

import java.util.List;

public interface HistoryService {

    List<HistoryLine> getPlayerHistory(OfflinePlayer player);

    void createHistoryLine(OfflinePlayer player, HistoryLine history);

    void deleteHistory(OfflinePlayer target);
}
