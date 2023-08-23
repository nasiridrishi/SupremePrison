package dev.drawethree.xprison.autosell.listener;

import dev.drawethree.xprison.autosell.XPrisonAutoSell;
import dev.drawethree.xprison.autosell.model.SellRegion;
import dev.drawethree.xprison.utils.player.PlayerUtils;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class AutoSellListener {

    private final XPrisonAutoSell plugin;

    public AutoSellListener(XPrisonAutoSell plugin) {
        this.plugin = plugin;
    }

    public void subscribeToEvents() {
        this.subscribeToPlayerJoinEvent();
        this.subscribeToBlockBreakEvent();
        this.subscribeToWorldLoadEvent();
    }

    private void subscribeToWorldLoadEvent() {
        Events.subscribe(WorldLoadEvent.class)
                .handler(e -> this.plugin.getManager().loadPostponedAutoSellRegions(e.getWorld())).bindWith(this.plugin.getCore());
    }

    private void subscribeToPlayerJoinEvent() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(e -> Schedulers.sync().runLater(() -> {

                    if (this.plugin.getManager().hasAutoSellEnabled(e.getPlayer())) {
                        PlayerUtils.sendMessage(e.getPlayer(), this.plugin.getAutoSellConfig().getMessage("autosell_enable"));
                        return;
                    }

                    if (this.plugin.getManager().canPlayerEnableAutosellOnJoin(e.getPlayer())) {
                        this.plugin.getManager().toggleAutoSell(e.getPlayer());
                    }
                }, 20)).bindWith(this.plugin.getCore());
    }

    private void subscribeToBlockBreakEvent() {
        Events.subscribe(BlockBreakEvent.class, EventPriority.HIGHEST)
                .filter(e -> {
                    if (e.isCancelled()) return false;
                    return this.plugin.getCore().isPickaxeSupported(e.getPlayer().getItemInHand().getType());
                })
                .handler(e -> {
                    //check if inventory is full
                    if (e.getPlayer().getInventory().firstEmpty() != -1) {
                        return;
                    }
                    SellRegion sellRegion = this.plugin.getManager().getAutoSellRegion(e.getBlock().getLocation());
                    if (sellRegion == null) {
                        return;
                    }
                    e.setCancelled(true);
                    plugin.getManager().sellAll(e.getPlayer(), sellRegion);
                }).bindWith(this.plugin.getCore());
    }
}

