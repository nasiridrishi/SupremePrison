package dev.drawethree.xprison.ranks.manager;

import dev.drawethree.xprison.economy.PrisonCurrency;
import dev.drawethree.xprison.economy.PrisonEconomyManager;
import dev.drawethree.xprison.mines.model.mine.Mine;
import dev.drawethree.xprison.prestiges.XPrisonPrestiges;
import dev.drawethree.xprison.prestiges.manager.PrestigeManager;
import dev.drawethree.xprison.prestiges.model.Prestige;
import dev.drawethree.xprison.ranks.XPrisonRanks;
import dev.drawethree.xprison.ranks.api.events.PlayerRankUpEvent;
import dev.drawethree.xprison.ranks.model.Rank;
import dev.drawethree.xprison.utils.item.ItemStackBuilder;
import dev.drawethree.xprison.utils.misc.MathUtils;
import dev.drawethree.xprison.utils.misc.ProgressBar;
import dev.drawethree.xprison.utils.player.PlayerUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.menu.Item;
import me.lucko.helper.menu.paginated.PaginatedGuiBuilder;
import me.lucko.helper.utils.Players;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RanksManager {

    private final XPrisonRanks plugin;
    private final Map<UUID, Integer> onlinePlayersRanks;

    public RanksManager(XPrisonRanks plugin) {
        this.plugin = plugin;
        this.onlinePlayersRanks = new ConcurrentHashMap<>();
    }

    private void saveAllDataSync() {
        for (UUID uuid : this.onlinePlayersRanks.keySet()) {
            this.plugin.getRanksService().setRank(Players.getOfflineNullable(uuid), onlinePlayersRanks.get(uuid));
        }
        this.plugin.getCore().getLogger().info("Saved online players ranks.");
    }

    private void loadAllData() {
        loadPlayerRank(Players.all());
    }

    public void savePlayerRank(Player player) {
        Schedulers.async().run(() -> {
            this.plugin.getRanksService().setRank(player, this.getPlayerRank(player).getId());
            this.onlinePlayersRanks.remove(player.getUniqueId());
            this.plugin.getCore().debug("Saved " + player.getName() + "'s rank.", this.plugin);
        });
    }

    public void loadPlayerRank(Collection<Player> players) {
        Schedulers.async().run(() -> {
            for (Player player : players) {
                this.plugin.getRanksService().createRank(player);
                int rank = this.plugin.getRanksService().getPlayerRank(player);
                this.onlinePlayersRanks.put(player.getUniqueId(), rank);
                this.plugin.getCore().debug("Loaded " + player.getName() + "'s rank.", this.plugin);
            }
        });
    }

    public Optional<Rank> getNextRank(int id) {
        return this.getRankById(id + 1);
    }

    public Rank getPlayerRank(Player p) {
        int rankId = this.onlinePlayersRanks.getOrDefault(p.getUniqueId(), getDefaultRank().getId());
        return this.getRankById(rankId).orElse(this.getDefaultRank());
    }

    private Rank getDefaultRank() {
        return this.plugin.getRanksConfig().getDefaultRank();
    }

    public boolean isMaxRank(Player p) {
        return this.getPlayerRank(p).getId() == getMaxRank().getId();
    }

//    public boolean buyMaxRank(Player p) {
//        if (isMaxRank(p)) {
//            PlayerUtils.sendMessage(p, this.plugin.getRanksConfig().getMessage("prestige_needed"));
//            return false;
//        }
//
//        Rank maxRank = getMaxRank();
//        Rank currentRank = this.getPlayerRank(p);
//
//        int finalRankId = currentRank.getId();
//
//        for (int i = currentRank.getId(); i < maxRank.getId(); i++) {
//            Optional<Rank> rank = this.getRankById(i + 1);
//            if (!rank.isPresent()) {
//                break;
//            }
//            double cost = this.getRankCost(rank.get(), p);
//            if (!this.isTransactionAllowed(p, cost)) {
//                break;
//            }
//            if (!this.completeTransaction(p, cost)) {
//                break;
//            }
//            finalRankId = i + 1;
//        }
//
//        Optional<Rank> nextRankOptional = this.getNextRank(currentRank.getId());
//
//        if (finalRankId == currentRank.getId() && nextRankOptional.isPresent()) {
//            PlayerUtils.sendMessage(p, this.plugin.getRanksConfig().getMessage("not_enough_money").replace("%cost%", String.format("%,.0f", this.getRankCost(nextRankOptional.get(), p))));
//            return false;
//        }
//
//        Optional<Rank> finalRankOptional = this.getRankById(finalRankId);
//
//        if (!finalRankOptional.isPresent()) {
//            return false;
//        }
//
//        Rank finalRank = finalRankOptional.get();
//
//        PlayerRankUpEvent event = new PlayerRankUpEvent(p, currentRank, finalRank);
//
//        Events.call(event);
//
//        if (event.isCancelled()) {
//            this.plugin.getCore().debug("PlayerRankUpEvent was cancelled.", this.plugin);
//            return false;
//        }
//
//        AtomicReference<Rank> stepPreviousRank = new AtomicReference<>(currentRank);
//
//        for (int i = currentRank.getId() + 1; i <= finalRank.getId(); i++) {
//            this.getRankById(i).ifPresent(r -> {
//                runCommands(stepPreviousRank.get(), r, p);
//                stepPreviousRank.set(r);
//            });
//        }
//
//        this.onlinePlayersRanks.put(p.getUniqueId(), finalRank.getId());
//        PlayerUtils.sendMessage(p, this.plugin.getRanksConfig().getMessage("rank_up").replace("%Rank-1%", currentRank.getPrefix()).replace("%Rank-2%", finalRank.getPrefix()));
//        return true;
//    }

    private Rank getMaxRank() {
        return this.plugin.getRanksConfig().getMaxRank();
    }

    public boolean buyNextRank(Player p) {

        if (isMaxRank(p)) {
            PlayerUtils.sendMessage(p, this.plugin.getRanksConfig().getMessage("prestige_needed"));
            return false;
        }

        Rank currentRank = this.getPlayerRank(p);
        Optional<Rank> toBuyOptional = getNextRank(currentRank.getId());

        if (!toBuyOptional.isPresent()) {
            PlayerUtils.sendMessage(p, this.plugin.getRanksConfig().getMessage("prestige_needed"));
            return false;
        }

        Rank toBuy = toBuyOptional.get();
        double toBuyCost = this.getRankCost(toBuy, p);
        if (!this.isTransactionAllowed(p, toBuyCost)) {
            if (this.plugin.getRanksConfig().isUseTokensCurrency()) {
                PlayerUtils.sendMessage(p, this.plugin.getRanksConfig().getMessage("not_enough_tokens").replace("%cost%", String.format("%,.0f", toBuyCost)));
            } else {
                PlayerUtils.sendMessage(p, this.plugin.getRanksConfig().getMessage("not_enough_money").replace("%cost%", String.format("%,.0f", toBuyCost)));
            }
            return false;
        }

        PlayerRankUpEvent event = new PlayerRankUpEvent(p, currentRank, toBuy);

        Events.call(event);

        if (event.isCancelled()) {
            this.plugin.getCore().debug("PlayerRankUpEvent was cancelled.", this.plugin);
            return false;
        }

        if (!this.completeTransaction(p, toBuyCost)) {
            return false;
        }

        runCommands(currentRank, toBuy, toBuyCost, p);

        this.onlinePlayersRanks.put(p.getUniqueId(), toBuy.getId());

        PlayerUtils.sendMessage(p, this.plugin.getRanksConfig().getMessage("rank_up").replace("%Rank-1%", currentRank.getPrefix()).replace("%Rank-2%", toBuy.getPrefix()));
        return true;
    }

    private boolean completeTransaction(Player p, double cost) {
        if (this.plugin.getRanksConfig().isUseTokensCurrency()) {
            PrisonEconomyManager.getInstance().removeBalance(PrisonCurrency.TOKEN, p, (long) cost);
            return true;
        } else {
            return this.plugin.getCore().getEconomy().withdrawPlayer(p, cost).transactionSuccess();
        }
    }

    private boolean isTransactionAllowed(Player p, double cost) {
        if (this.plugin.getRanksConfig().isUseTokensCurrency()) {
            return PrisonEconomyManager.getInstance().getBalance(PrisonCurrency.TOKEN, p) >= cost;
        } else {
            return this.plugin.getCore().getEconomy().has(p, cost);
        }
    }


    public void setRank(Player target, Rank rank, CommandSender sender) {

        Rank currentRank = this.getPlayerRank(target);

        PlayerRankUpEvent event = new PlayerRankUpEvent(target, currentRank, rank);

        Events.call(event);

        if (event.isCancelled()) {
            this.plugin.getCore().debug("PlayerRankUpEvent was cancelled.", this.plugin);
            return;
        }

        this.runCommands(currentRank, rank, 0.0, target);

        this.onlinePlayersRanks.put(target.getUniqueId(), rank.getId());

        if (sender != null) {
            PlayerUtils.sendMessage(sender, this.plugin.getRanksConfig().getMessage("rank_set").replace("%rank%", rank.getPrefix()).replace("%player%", target.getName()));
            PlayerUtils.sendMessage(target, this.plugin.getRanksConfig().getMessage("rank_up").replace("%Rank-1%", currentRank.getPrefix()).replace("%Rank-2%", rank.getPrefix()));
        }

    }

    public int getRankupProgress(Player player) {

        if (this.isMaxRank(player)) {
            if (arePrestigesEnabled()) {
                return getPrestigeManager().getPrestigeProgress(player);
            }
            return 100;
        }

        Rank current = this.getPlayerRank(player);
        Optional<Rank> nextRankOptional = this.getNextRank(current.getId());

        if (!nextRankOptional.isPresent()) {
            return 100;
        }

        double currentBalance = this.getBalance(player);

        int progress = (int) ((currentBalance / getNextRankCost(player)) * 100);

        if (progress > 100) {
            progress = 100;
        }

        return progress;
    }

    public double getNextRankCost(Player player) {
        if (this.isMaxRank(player)) {
            if (arePrestigesEnabled()) {
                if (getPrestigeManager().isMaxPrestige(player)) {
                    return 0.0;
                } else {
                    Prestige prestige = getPrestigeManager().getPlayerPrestige(player);
                    Prestige next = getPrestigeManager().getNextPrestige(prestige);
                    if (next != null) {
                        return next.getCost();
                    } else {
                        return 0.0;
                    }
                }
            } else {
                return 0.0;
            }
        }
        Rank current = this.getPlayerRank(player);
        Optional<Rank> nextRankOptional = this.getNextRank(current.getId());
        return nextRankOptional.map(rank -> getRankCost(rank, player)).orElse(0.0);
    }

    public void resetPlayerRank(Player p) {
        setRank(p, getDefaultRank(), null);
    }

    private boolean arePrestigesEnabled() {
        return this.plugin.getCore().isModuleEnabled(XPrisonPrestiges.MODULE_NAME);
    }

    private PrestigeManager getPrestigeManager() {
        if (!arePrestigesEnabled()) {
            throw new IllegalStateException("Prestiges module is not enabled");
        }
        return this.plugin.getCore().getPrestiges().getPrestigeManager();
    }

    public String getRankupProgressBar(Player player) {
        return ProgressBar.getProgressBar(this.plugin.getRanksConfig().getProgressBarLength(), this.plugin.getRanksConfig().getProgressBarDelimiter(), getRankupProgress(player), 100);
    }

    private double getBalance(Player player) {
        return this.plugin.getRanksConfig().isUseTokensCurrency() ? PrisonEconomyManager.getInstance().getBalance(PrisonCurrency.TOKEN, player) : this.plugin.getCore().getEconomy().getBalance(player);
    }

    public void runCommands(Rank oldRank, Rank newRank, double rankupCost, Player p) {
        if (newRank.getCommandsToExecute() != null) {

            if (!Bukkit.isPrimaryThread()) {
                Schedulers.async().run(() -> {
                    executeCommands(oldRank, newRank, rankupCost, p);
                });
            } else {
                executeCommands(oldRank, newRank, rankupCost, p);
            }
        }
    }

    public void openRankMinesListGUI(Player player) {
        PaginatedGuiBuilder builder = PaginatedGuiBuilder.create();
        builder.lines(6);
        builder.title("&6Prisoner Mines");
        builder.nextPageSlot(53);
        builder.previousPageSlot(44);
        builder.nextPageItem((pageInfo) -> ItemStackBuilder.of(Material.ARROW).name("&aNext Page").lore("&7Click to see next page.").build());
        builder.previousPageItem((pageInfo) -> ItemStackBuilder.of(Material.ARROW).name("&aPrevious Page").lore("&7Click to see previous page.").build());

        builder.build(player, paginatedGui -> {
            List<Item> items = new ArrayList<>();
            for (Rank rank : this.plugin.getRanksConfig().getShortedRanksById().values()) {
                if (rank.getMine() != null) {
                    if (this.canRankMineTP(rank, player)) {
                        items.add(ItemStackBuilder.of(Material.GREEN_WOOL).name("&7 Prison Mine: &8[&a&l" + rank.getPrefix() + " RANK &8]").lore("&e", "&aClick to teleport to this mine.", "&7Rank Required &8> &a" + rank.getName(), "&7Rank Cost &8> &a$" + MathUtils.formatNumber(getRankCost(rank, player))).durability(5).build(() -> this.attemptRankMineTeleport(rank, player, 5)));
                    } else {
                        items.add(ItemStackBuilder.of(Material.RED_WOOL).name("&7 Prison Mine: &8[&c&l" + rank.getPrefix() + " RANK &8]").lore("&e", "&cYou do not have permission on this mine.", "&7Rank Required &8> &c" + rank.getName(), "&7Rank Cost &8> &c$" + MathUtils.formatNumber(getRankCost(rank, player))).durability(14).build(() -> openRankMinesListGUI(player)));
                    }
                } else {
                    items.add(ItemStackBuilder.of(Material.BARRIER).name("&c" + rank.getPrefix()).lore("&4This rank does not have a mine.").build(() -> openRankMinesListGUI(player)));
                }
            }
            return items;
        }).open();

    }

    public boolean hasMine(Rank rank) {
        return rank.getMine() != null;
    }

    public boolean canRankMineTP(Rank rank, Player player) {
        return this.hasMine(rank) && this.getPlayerRank(player).getId() >= rank.getId();
    }

    public boolean attemptRankMineTeleport(Rank rank, Player player, int cooldown) {
        Mine mine = rank.getMine();
        if (mine == null) {
            player.sendMessage(this.plugin.getRanksConfig().getMessage("rank_mine_not_found").replace("%rank%", rank.getPrefix()));
            this.plugin.getCore().getLogger().severe("Attempted to teleport player to a mine that does not exist! Rank: " + rank.getPrefix());
            return false;
        } else {
            Rank playerRank = this.getPlayerRank(player);
            if (playerRank.getId() >= rank.getId()) {
                player.teleport(mine.getTeleportLocation().toLocation());
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            } else {
                player.sendMessage(this.plugin.getRanksConfig().getMessage("rank_mine_not_unlocked").replace("%rank%", rank.getPrefix()));
                return false;
            }
        }
        return true;
    }

    public Optional<Rank> getRankById(int id) {
        return Optional.ofNullable(this.plugin.getRanksConfig().getRankById(id));
    }

    private void executeCommands(Rank oldRank, Rank rank, double rankUPCost, Player p) {
        for (String cmd : rank.getCommandsToExecute()) {
            cmd = cmd.replace("%player%", p.getName());
            cmd = cmd.replace("%rank%", rank.getPrefix());
            cmd = cmd.replace("%oldRank%", oldRank.getPrefix());
            cmd = cmd.replace("%newRank%", rank.getPrefix());
            cmd = cmd.replace("%rankupCost%", String.valueOf(rankUPCost));
            cmd = PlaceholderAPI.setPlaceholders(p, cmd);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }

    public void disable() {
        this.saveAllDataSync();
    }

    public void enable() {
        this.loadAllData();
    }


    public double getRankCost(Rank rank, Player player) {
        if (arePrestigesEnabled()) {
            return plugin.getCore().getPrestiges().getPrestigeManager().getRankUpCost(rank, player);
        }
        return rank.getCost();
    }

    public String getRankUpStatusText(Player player) {
        double rankUpCost = getNextRankCost(player);
        double balance = getBalance(player);
        if (balance >= rankUpCost) {
            return "/rankup";
        }
        return ChatColor.RED + String.valueOf(balance) + ChatColor.YELLOW + "/" + ChatColor.GOLD + rankUpCost;
    }

}
