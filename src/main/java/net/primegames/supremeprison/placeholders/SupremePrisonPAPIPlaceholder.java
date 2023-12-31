package net.primegames.supremeprison.placeholders;

import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.autominer.utils.AutoMinerUtils;
import net.primegames.supremeprison.gangs.model.Gang;
import net.primegames.supremeprison.mines.model.mine.Mine;
import net.primegames.supremeprison.multipliers.multiplier.GlobalMultiplier;
import net.primegames.supremeprison.multipliers.multiplier.Multiplier;
import net.primegames.supremeprison.multipliers.multiplier.PlayerMultiplier;
import net.primegames.supremeprison.pickaxelevels.model.PickaxeLevel;
import net.primegames.supremeprison.ranks.model.Rank;
import net.primegames.supremeprison.utils.misc.MathUtils;
import net.primegames.supremeprison.utils.misc.TimeUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * This class will be registered through the register-method in the
 * plugins onEnable-method.
 */
public final class SupremePrisonPAPIPlaceholder extends PlaceholderExpansion {

    private final SupremePrison plugin;

    /**
     * Since we register the expansion inside our own plugin, we
     * can simply use this method here to get an instance of our
     * plugin.
     *
     * @param plugin The instance of our plugin.
     */
    public SupremePrisonPAPIPlaceholder(SupremePrison plugin) {
        this.plugin = plugin;
    }

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist() {
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br>For convienience do we return the author from the plugin.yml
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier() {
        return "supremeprison";
    }

    /**
     * This is the version of the expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     * <p>
     * For convienience do we return the version from the plugin.yml
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param player
     * @param identifier A String containing the identifier/value.
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return null;
        }

        if (identifier.startsWith("mine_") && plugin.getMines().isEnabled()) {
            String mineName = identifier.replace("mine_", "").split("_")[0];
            Mine mine = plugin.getMines().getManager().getMineByName(mineName);

            if (mine == null) {
                return null;
            }

            String placeholder = identifier.replace("mine_" + mineName + "_", "");
            switch (placeholder.toLowerCase()) {
                case "blocks_left": {
                    return String.format("%,d", mine.getCurrentBlocks());
                }
                case "blocks_left_percentage": {
                    return String.format("%,.2f", (double) mine.getCurrentBlocks() / mine.getTotalBlocks() * 100.0D);
                }
                case "reset_time": {
                    return TimeUtil.getTime(mine.getSecondsToNextReset());
                }
            }
        }

        switch (identifier.toLowerCase()) {
            case "tokens":
            case "tokens_2":
                return String.format("%,d", plugin.getTokens().getTokensManager().getPlayerTokens(player));
            case "blocks":
            case "blocks_2":
                return String.format("%,d", plugin.getTokens().getTokensManager().getPlayerBrokenBlocks(player));
            case "multiplier_sell": {
                PlayerMultiplier sellMulti = plugin.getMultipliers().getApi().getSellMultiplier(player);
                if (sellMulti == null || sellMulti.isExpired()) {
                    return String.format("%.2f", 0.0);
                } else {
                    return String.format("%.2f", (1.0 + sellMulti.getMultiplier()));
                }
            }
            case "multiplier_token": {
                PlayerMultiplier tokenMulti = plugin.getMultipliers().getApi().getTokenMultiplier(player);
                if (tokenMulti == null || tokenMulti.isExpired()) {
                    return String.format("%.2f", 0.0);
                } else {
                    return String.format("%.2f", (1.0 + tokenMulti.getMultiplier()));
                }
            }
            case "multiplier_rank": {
                Multiplier rankMulti = plugin.getMultipliers().getApi().getRankMultiplier(player);
                if (rankMulti == null) {
                    return String.format("%.2f", 0.0);
                } else {
                    return String.format("%.2f", rankMulti.getMultiplier());
                }
            }
            case "multiplier_global_sell": {
                GlobalMultiplier sellMulti = plugin.getMultipliers().getApi().getGlobalSellMultiplier();
                return String.format("%.2f", sellMulti.isExpired() ? 0.0 : sellMulti.getMultiplier());
            }
            case "multiplier_global_token": {
                GlobalMultiplier tokenMulti = plugin.getMultipliers().getApi().getGlobalTokenMultiplier();
                return String.format("%.2f", tokenMulti.isExpired() ? 0.0 : tokenMulti.getMultiplier());
            }
            case "rank":
                return plugin.getRanks().getApi().getPlayerRank(player).getName();
            case "rank_prefix":
                return plugin.getRanks().getApi().getPlayerRank(player).getPrefix();
            case "next_rank": {
                Optional<Rank> nextRank = plugin.getRanks().getApi().getNextPlayerRank(player);
                return !nextRank.isPresent() ? "" : nextRank.get().getName();
            }
            case "next_rank_prefix": {
                Optional<Rank> nextRank = plugin.getRanks().getApi().getNextPlayerRank(player);
                return !nextRank.isPresent() ? "" : nextRank.get().getPrefix();
            }
            case "next_rank_cost_raw":
                return String.valueOf(plugin.getRanks().getRanksManager().getNextRankCost(player));
            case "next_rank_cost":
                return String.format("%,.2f", plugin.getRanks().getRanksManager().getNextRankCost(player));
            case "next_rank_cost_formatted":
                return MathUtils.formatNumber(plugin.getRanks().getRanksManager().getNextRankCost(player));
            case "rankup_status_text":
                return plugin.getRanks().getRanksManager().getRankUpStatusText(player);
            case "prestige":
                return plugin.getPrestiges().getApi().getPlayerPrestige(player).getPrefix();
            case "prestige_id":
                return String.valueOf(plugin.getPrestiges().getApi().getPlayerPrestige(player).getId());
            case "autominer_time": {
                int autominerTime = plugin.getAutoMiner().getManager().getAutoMinerTime(player);
                return AutoMinerUtils.getAutoMinerTimeLeftFormatted(autominerTime);
            }
            case "tokens_formatted":
            case "tokens_3":
                return MathUtils.formatNumber(plugin.getTokens().getTokensManager().getPlayerTokens(player));
            case "gems_formatted":
            case "rankup_progress":
                return String.valueOf(plugin.getRanks().getRanksManager().getRankupProgress(player));
            case "rankup_progress_bar":
                return plugin.getRanks().getRanksManager().getRankupProgressBar(player);
//            case "rankup_bossbar_text": {
//                //if can rank up print /rankup else print balance/rank up cost
//                return plugin.getRanks().getRanksManager().getBossBarProgressText(player);
//            }
            case "tokens_1":
                return String.valueOf(plugin.getTokens().getTokensManager().getPlayerTokens(player));
            case "blocks_1":
                return String.valueOf(plugin.getTokens().getTokensManager().getPlayerBrokenBlocks(player));
            case "blocks_3":
                return MathUtils.formatNumber(plugin.getTokens().getTokensManager().getPlayerBrokenBlocks(player));
            case "pickaxe_level": {
                Optional<PickaxeLevel> levelOptional = plugin.getPickaxeLevels().getApi().getPickaxeLevel(player);
                return levelOptional.map(level -> String.valueOf(level.getLevel())).orElse("0");
            }
            case "pickaxe_progress":
                return this.plugin.getPickaxeLevels().getApi().getProgressBar(player);
            case "gang_name":
            case "gang": {
                Optional<Gang> optionalGang = this.plugin.getGangs().getGangsManager().getPlayerGang(player);
                return optionalGang.map(gang -> this.plugin.getGangs().getConfig().getPlaceholder("gang-in-gang").replace("%gang%", gang.getName())).orElseGet(() -> this.plugin.getGangs().getConfig().getPlaceholder("gang-without"));
            }
            case "gang_value": {
                Optional<Gang> optionalGang = this.plugin.getGangs().getGangsManager().getPlayerGang(player);
                if (optionalGang.isPresent()) {
                    return String.format("%,d", optionalGang.get().getValue());
                } else {
                    return "";
                }
            }
            case "gang_has_gang":
                return this.plugin.getGangs().getGangsManager().getPlayerGang(player).isPresent() ? "Yes" : "No";
            case "gang_is_leader": {
                Optional<Gang> optionalGang = this.plugin.getGangs().getGangsManager().getPlayerGang(player);
                return optionalGang.map(gang -> gang.isOwner(player) ? "Yes" : "No").orElse("");
            }
            case "gang_leader_name": {
                Optional<Gang> optionalGang = this.plugin.getGangs().getGangsManager().getPlayerGang(player);
                if (optionalGang.isPresent()) {
                    return optionalGang.get().getOwnerOffline().getName();
                }
                return "";
            }
            case "gang_members_amount": {
                Optional<Gang> optionalGang = this.plugin.getGangs().getGangsManager().getPlayerGang(player);
                // +1 because of leader
                return optionalGang.map(gang -> String.valueOf(gang.getMembersOffline().size() + 1)).orElse("");
            }
            case "gang_members_online": {
                Optional<Gang> optionalGang = this.plugin.getGangs().getGangsManager().getPlayerGang(player);
                return optionalGang.map(gang -> String.valueOf(gang.getOnlinePlayers().size())).orElse("");
            }
            default:
                return null;
        }
    }
}