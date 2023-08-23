package net.primegames.supremeprison.history.repo;

import net.primegames.supremeprison.history.model.HistoryLine;
import org.bukkit.OfflinePlayer;

import java.util.List;

public interface HistoryRepository {

    List<HistoryLine> getPlayerHistory(OfflinePlayer player);

    void addHistoryLine(OfflinePlayer player, HistoryLine history);

    void deleteHistory(OfflinePlayer target);

    void createTables();

    void clearTableData();
}
