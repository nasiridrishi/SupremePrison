package dev.drawethree.xprison.placeholders;

import dev.drawethree.xprison.ranks.XPrisonRanks;
import dev.drawethree.xprison.ranks.model.Rank;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class xPrisonPapiRanksPlaceHolder extends PlaceholderExpansion {

    private final ArrayList<String> ranks = new ArrayList<>();
    private final XPrisonRanks plugin;


    public xPrisonPapiRanksPlaceHolder(XPrisonRanks plugin) {
        plugin.getRanksConfig().getRanksById().forEach((k, v) -> ranks.add(v.getName().toLowerCase()));
        this.plugin = plugin;
    }


    @Override
    public @NotNull String getIdentifier() {
        return "prisonrank";
    }

    @Override
    public @NotNull String getAuthor() {
        return "drawethree";
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
