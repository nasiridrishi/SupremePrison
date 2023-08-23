package net.primegames.supremeprison.ranks.api;

import net.primegames.supremeprison.ranks.manager.RanksManager;
import net.primegames.supremeprison.ranks.model.Rank;
import org.bukkit.entity.Player;

import java.util.Optional;

public final class SupremePrisonRanksAPIImpl implements SupremePrisonRanksAPI {

    private final RanksManager manager;

    public SupremePrisonRanksAPIImpl(RanksManager manager) {
        this.manager = manager;
    }

    @Override
    public Rank getPlayerRank(Player p) {
        return manager.getPlayerRank(p);
    }

    @Override
    public Optional<Rank> getNextPlayerRank(Player player) {
        return manager.getNextRank(this.getPlayerRank(player).getId());
    }

    @Override
    public int getRankupProgress(Player player) {
        return manager.getRankupProgress(player);
    }

    @Override
    public void setPlayerRank(Player player, Rank rank) {
        manager.setRank(player, rank, null);
    }
}
