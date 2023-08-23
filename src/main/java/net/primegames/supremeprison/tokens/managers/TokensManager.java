package net.primegames.supremeprison.tokens.managers;

import net.primegames.supremeprison.api.enums.ReceiveCause;
import net.primegames.supremeprison.economy.PrisonCurrency;
import net.primegames.supremeprison.economy.PrisonEconomyManager;
import net.primegames.supremeprison.tokens.SupremePrisonTokens;
import net.primegames.supremeprison.tokens.api.events.PlayerTokensReceiveEvent;
import net.primegames.supremeprison.tokens.api.events.SupremePrisonBlockBreakEvent;
import net.primegames.supremeprison.tokens.model.BlockReward;
import net.primegames.supremeprison.utils.misc.NumberUtils;
import net.primegames.supremeprison.utils.player.PlayerUtils;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.time.Time;
import me.lucko.helper.utils.Players;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class TokensManager {

    private final SupremePrisonTokens plugin;
    private final Map<UUID, Long> blocksCache;
    private final Map<UUID, Long> blocksCacheWeekly;
    private final List<UUID> tokenMessageOnPlayers;

    public TokensManager(SupremePrisonTokens plugin) {
        this.plugin = plugin;
        this.tokenMessageOnPlayers = new ArrayList<>();
        this.blocksCache = new ConcurrentHashMap<>();
        this.blocksCacheWeekly = new ConcurrentHashMap<>();
    }

    public void savePlayerData(Collection<Player> players, boolean removeFromCache, boolean async) {
        if (async) {
            Schedulers.async().run(() -> savePlayerDataLogic(players, removeFromCache));
        } else {
            savePlayerDataLogic(players, removeFromCache);
        }
    }

    private void savePlayerDataLogic(Collection<Player> players, boolean removeFromCache) {
        for (Player player : players) {
            this.plugin.getBlocksService().setBlocks(player, blocksCache.getOrDefault(player.getUniqueId(), 0L));
            this.plugin.getBlocksService().setBlocksWeekly(player, blocksCacheWeekly.getOrDefault(player.getUniqueId(), 0L));

            if (removeFromCache) {
                this.blocksCache.remove(player.getUniqueId());
                this.blocksCacheWeekly.remove(player.getUniqueId());
            }

            this.plugin.getCore().debug(String.format("Saved player %s tokens & blocks broken to database.", player.getName()), this.plugin);
        }
    }

    public void savePlayerDataOnDisable() {
        for (UUID uuid : blocksCache.keySet()) {
            this.plugin.getBlocksService().setBlocks(Players.getOfflineNullable(uuid), blocksCache.get(uuid));
        }
        for (UUID uuid : blocksCache.keySet()) {
            this.plugin.getBlocksService().setBlocksWeekly(Players.getOfflineNullable(uuid), blocksCacheWeekly.get(uuid));
        }
        blocksCache.clear();
        blocksCacheWeekly.clear();
        this.plugin.getCore().getLogger().info("Saved online player tokens, blocks broken and weekly blocks broken.");

    }

    private void loadPlayerDataOnEnable() {
        loadPlayerData(Players.all());
    }

    public void loadPlayerData(Collection<Player> players) {
        Schedulers.async().run(() -> {
            for (Player player : players) {
                this.plugin.getBlocksService().createBlocks(player);
                this.plugin.getBlocksService().createBlocksWeekly(player);
                long playerBlocks = this.plugin.getBlocksService().getPlayerBrokenBlocks(player);
                long playerBlocksWeekly = this.plugin.getBlocksService().getPlayerBrokenBlocksWeekly(player);
                this.blocksCache.put(player.getUniqueId(), playerBlocks);
                this.blocksCacheWeekly.put(player.getUniqueId(), playerBlocksWeekly);

                this.plugin.getCore().debug(String.format("Loaded tokens and blocks broken of player %s from database", player.getName()), this.plugin);
            }
        });
    }

    public void giveTokens(OfflinePlayer p, long amount, CommandSender executor, ReceiveCause cause, Location dropOnGround) {
        double currentTokens = PrisonEconomyManager.instance.getBalance(PrisonCurrency.TOKEN, p);

        this.plugin.getCore().debug("SupremePrisonPlayerTokenReceiveEvent :: Player Tokens :: " + currentTokens, this.plugin);

        long finalAmount = this.callTokensReceiveEvent(cause, p, amount);

        this.plugin.getCore().debug("SupremePrisonPlayerTokenReceiveEvent :: Final amount :: " + finalAmount, this.plugin);

        double newAmount;

        if (NumberUtils.wouldAdditionBeOverMaxLong((long) currentTokens, finalAmount)) {
            newAmount = Long.MAX_VALUE;
        } else {
            newAmount = currentTokens + finalAmount;
        }

        double amountToGive = newAmount - currentTokens;

        if (dropOnGround != null && dropOnGround.isWorldLoaded()) {
            PrisonEconomyManager.getInstance().dropBalanceAsItem(Objects.requireNonNull(dropOnGround.getWorld()), dropOnGround, PrisonCurrency.TOKEN, amountToGive);
        } else {
            PrisonEconomyManager.instance.setBalance(PrisonCurrency.TOKEN, p, newAmount);
        }

        if (p.isOnline()) {
            if (executor instanceof ConsoleCommandSender && !this.hasOffTokenMessages(p.getPlayer())) {
                PlayerUtils.sendMessage(p.getPlayer(), plugin.getTokensConfig().getMessage("tokens_received_console").replace("%tokens%", String.format("%,d", finalAmount)).replace("%player%", executor == null ? "Console" : executor.getName()));
            } else if (cause == ReceiveCause.MINING && !this.hasOffTokenMessages(p.getPlayer())) {
                PlayerUtils.sendMessage(p.getPlayer(), this.plugin.getTokensConfig().getMessage("tokens_received_mining").replace("%amount%", String.format("%,d", finalAmount)));
            } else if (cause == ReceiveCause.LUCKY_BLOCK && !this.hasOffTokenMessages(p.getPlayer())) {
                PlayerUtils.sendMessage(p.getPlayer(), this.plugin.getTokensConfig().getMessage("lucky_block_mined").replace("%amount%", String.format("%,d", finalAmount)));
            }
        }

        this.plugin.getCore().debug("XPlayerTokenReceiveEvent :: Player tokens final  :: " + PrisonEconomyManager.instance.getBalance(PrisonCurrency.TOKEN, p), this.plugin);

        if (executor != null && !(executor instanceof ConsoleCommandSender)) {
            PlayerUtils.sendMessage(executor, plugin.getTokensConfig().getMessage("admin_give_tokens").replace("%player%", p.getName()).replace("%tokens%", String.format("%,d", finalAmount)));
        }
    }

    private long callTokensReceiveEvent(ReceiveCause cause, OfflinePlayer p, long amount) {
        PlayerTokensReceiveEvent event = new PlayerTokensReceiveEvent(cause, p, amount);
        Events.callSync(event);
        if (event.isCancelled()) {
            return amount;
        }
        return event.getAmount();
    }

    public synchronized long getPlayerTokens(OfflinePlayer p) {
        return (long) PrisonEconomyManager.instance.getBalance(PrisonCurrency.TOKEN, p);
    }

    public synchronized long getPlayerBrokenBlocks(OfflinePlayer p) {
        if (!p.isOnline()) {
            return this.plugin.getBlocksService().getPlayerBrokenBlocks(p);
        } else {
            return blocksCache.getOrDefault(p.getUniqueId(), (long) 0);
        }
    }

    public synchronized long getPlayerBrokenBlocksWeekly(OfflinePlayer p) {
        if (!p.isOnline()) {
            return this.plugin.getBlocksService().getPlayerBrokenBlocksWeekly(p);
        } else {
            return blocksCacheWeekly.getOrDefault(p.getUniqueId(), (long) 0);
        }
    }


    public void addBlocksBroken(OfflinePlayer player, List<Block> blocks) {
        if (player.isOnline()) {
            SupremePrisonBlockBreakEvent event = new SupremePrisonBlockBreakEvent((Player) player, blocks);
            Events.call(event);
            if (event.isCancelled()) {
                return;
            }
            blocks = event.getBlocks();
        }

        long finalAmount = blocks.size();
        Schedulers.async().run(() -> {
            long currentBroken = getPlayerBrokenBlocks(player);
            long currentBrokenWeekly = getPlayerBrokenBlocksWeekly(player);
            BlockReward nextReward = this.getNextBlockReward(player);

            if (!player.isOnline()) {
                this.plugin.getBlocksService().setBlocks(player, currentBroken + finalAmount);
                this.plugin.getBlocksService().setBlocksWeekly(player, currentBrokenWeekly + finalAmount);
            } else {
                blocksCache.put(player.getUniqueId(), currentBroken + finalAmount);
                blocksCacheWeekly.put(player.getUniqueId(), currentBrokenWeekly + finalAmount);

                while (nextReward != null && nextReward.getBlocksRequired() <= blocksCache.get(player.getUniqueId())) {
                    nextReward.giveTo((Player) player);
                    nextReward = this.getNextBlockReward(nextReward);
                }
            }
        });
    }

    private BlockReward getNextBlockReward(BlockReward oldReward) {
        boolean next = false;
        for (long l : this.plugin.getBlockRewardsConfig().getBlockRewards().keySet()) {
            if (next) {
                return this.plugin.getBlockRewardsConfig().getBlockRewards().get(l);
            }
            if (l == oldReward.getBlocksRequired()) {
                next = true;
            }
        }

        return null;
    }

    public BlockReward getNextBlockReward(OfflinePlayer p) {
        long blocksBroken = this.getPlayerBrokenBlocks(p);
        for (long l : this.plugin.getBlockRewardsConfig().getBlockRewards().keySet()) {
            if (l > blocksBroken) {
                return this.plugin.getBlockRewardsConfig().getBlockRewards().get(l);
            }
        }
        return null;
    }

    public void resetBlocksTopWeekly(CommandSender sender) {
        PlayerUtils.sendMessage(sender, "&7&oStarting to reset BlocksTop - Weekly. This may take a while...");
        this.plugin.getTokensConfig().setNextResetWeekly(Time.nowMillis() + TimeUnit.DAYS.toMillis(7));
        this.plugin.getBlocksService().resetBlocksWeekly();
        PlayerUtils.sendMessage(sender, "&aBlocksTop - Weekly - Reset!");
    }

    private void saveWeeklyReset() {
        this.plugin.getTokensConfig().getYamlConfig().set("next-reset-weekly", this.plugin.getTokensConfig().getNextResetWeekly());
        this.plugin.getTokensConfig().save();
    }

    public void reload() {

    }

    public void addPlayerIntoTokenMessageOnPlayers(Player player) {
        this.tokenMessageOnPlayers.add(player.getUniqueId());
    }

    public boolean hasOffTokenMessages(Player p) {
        return !this.tokenMessageOnPlayers.contains(p.getUniqueId());
    }

    public void disable() {
        this.saveWeeklyReset();
        this.savePlayerDataOnDisable();
    }


    public void handleBlockBreak(Player p, List<Block> blocks, boolean countBlocksBroken, Block targetBlock) {

        long startTime = System.currentTimeMillis();

        if (countBlocksBroken) {
            this.addBlocksBroken(p, blocks);
        }

        //now is being handled by ultracustomiser
//        //Lucky block check
//        blocks.forEach(block -> {
//            List<String> rewards = this.plugin.getTokensConfig().getLuckyBlockReward(block.getType());
//            for (String s : rewards) {
//                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", p.getName()));
//            }
//        });
//
//        long totalAmount = 0;
//        for (int i = 0; i < blocks.size(); i++) {
//            double random = ThreadLocalRandom.current().nextDouble(100);
//
//            if (this.plugin.getTokensConfig().getChance() >= random) {
//                long randAmount = this.plugin.getTokensConfig().getMinAmount() == this.plugin.getTokensConfig().getMaxAmount() ? this.plugin.getTokensConfig().getMinAmount() : ThreadLocalRandom.current().nextLong(this.plugin.getTokensConfig().getMinAmount(), this.plugin.getTokensConfig().getMaxAmount());
//                totalAmount += randAmount;
//            }
//        }
//        if (totalAmount > 0) {
//            this.giveTokens(p, totalAmount, null, ReceiveCause.MINING, (targetBlock != null) ? targetBlock.getLocation() : null);
//        }
        this.plugin.getCore().debug("SupremePrisonTokens::handleBlockBreak >> Took " + (System.currentTimeMillis() - startTime) + " ms.", this.plugin);
    }

    public void enable() {
        if (this.checkBlocksTopWeeklyReset()) {
            resetBlocksTopWeekly(Bukkit.getConsoleSender());
        }
        this.loadPlayerDataOnEnable();
    }

    private boolean checkBlocksTopWeeklyReset() {
        long nextResetWeeklyMillis = this.plugin.getTokensConfig().getNextResetWeekly();
        return Time.nowMillis() >= nextResetWeeklyMillis;
    }
}
