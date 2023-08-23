package net.primegames.supremeprison.placeholders;

import net.primegames.supremeprison.ranks.SupremePrisonRanks;
import net.primegames.supremeprison.ranks.model.Rank;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SupremePrisonPapiRanksPlaceHolder extends PlaceholderExpansion {

    private final ArrayList<String> ranks = new ArrayList<>();
    private final SupremePrisonRanks plugin;


    public SupremePrisonPapiRanksPlaceHolder(SupremePrisonRanks plugin) {
        plugin.getRanksConfig().getRanksById().forEach((k, v) -> ranks.add(v.getName().toLowerCase()));
        this.plugin = plugin;
    }


    @Override
    public @NotNull String getIdentifier() {
        return "prisonrank";
    }

    @Override
    public @NotNull String getAuthor() {
        return "supremeprison";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getCore().getDescription().getVersion();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "player not found";
        }
        params = params.toLowerCase();
        if (params.startsWith("rankupcost_") && plugin.getCore().isEnabled()) {
            String rank = params.replace("rankupcost_", "");
            if (ranks.contains(rank)) {
                Rank rank1 = plugin.getRanksConfig().getByName(rank);
                if (rank1 != null) {
                    return plugin.getRanksManager().getRankCost(rank1, player) + "";
                } else {
                    return "ERROR";
                }
            } else {
                return "invalid rank";
            }
        }
        return params;
    }
}
