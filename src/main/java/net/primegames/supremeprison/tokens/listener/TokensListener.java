package net.primegames.supremeprison.tokens.listener;

import net.primegames.supremeprison.tokens.SupremePrisonTokens;
import me.lucko.helper.Events;
import org.bukkit.block.Block;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TokensListener {

    private final SupremePrisonTokens plugin;

    public TokensListener(SupremePrisonTokens plugin) {

        this.plugin = plugin;
    }

    public void subscribeToEvents() {
        this.subscribeToPlayerJoinEvent();
        this.subscribeToPlayerQuitEvent();
        //this.subscribeToBlockBreakEvent();
    }

    private void subscribeToBlockBreakEvent() {
        Events.subscribe(BlockBreakEvent.class, EventPriority.HIGHEST)
                .filter(e -> !e.isCancelled())
                .filter(e -> this.plugin.getCore().isPickaxeSupported(e.getPlayer().getItemInHand().getType()))
                .handler(e -> {
                    List<Block> blocks = new ArrayList<>();
                    blocks.add(e.getBlock());
                    this.plugin.getTokensManager().handleBlockBreak(e.getPlayer(), blocks, true, e.getBlock());
                }).bindWith(plugin.getCore());
    }


    private void subscribeToPlayerQuitEvent() {
        Events.subscribe(PlayerQuitEvent.class)
                .handler(e -> {
                    e.getPlayer().getActivePotionEffects().forEach(effect -> e.getPlayer().removePotionEffect(effect.getType()));
                }).bindWith(plugin.getCore());
    }

    private void subscribeToPlayerJoinEvent() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(e -> {
                    this.plugin.getTokensManager().loadPlayerData(Collections.singleton(e.getPlayer()));

                    if (this.plugin.getTokensConfig().isDisplayTokenMessages() && this.plugin.getTokensManager().hasOffTokenMessages(e.getPlayer())) {
                        this.plugin.getTokensManager().addPlayerIntoTokenMessageOnPlayers(e.getPlayer());
                    }

                }).bindWith(plugin.getCore());
    }
}
