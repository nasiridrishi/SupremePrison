package net.primegames.supremeprison.history.api;

import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.history.SupremePrisonHistory;
import net.primegames.supremeprison.history.model.HistoryLine;
import org.bukkit.OfflinePlayer;

import java.util.List;

public final class SupremePrisonHistoryAPIImpl implements SupremePrisonHistoryAPI {

    private final SupremePrisonHistory plugin;

    public SupremePrisonHistoryAPIImpl(SupremePrisonHistory plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<HistoryLine> getPlayerHistory(OfflinePlayer player) {
        return this.plugin.getHistoryManager().getPlayerHistory(player);
    }

    @Override
    public void createHistoryLine(OfflinePlayer player, SupremePrisonModule module, String context) {
        this.plugin.getHistoryManager().createPlayerHistoryLine(player, module, context);
    }
}
