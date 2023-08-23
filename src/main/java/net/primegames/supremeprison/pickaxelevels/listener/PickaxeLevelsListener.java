package net.primegames.supremeprison.pickaxelevels.listener;

import net.primegames.supremeprison.pickaxelevels.SupremePrisonPickaxeLevels;
import me.lucko.helper.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public final class PickaxeLevelsListener {

    private final SupremePrisonPickaxeLevels plugin;

    public PickaxeLevelsListener(SupremePrisonPickaxeLevels plugin) {
        this.plugin = plugin;
    }

    public void register() {
        this.subscribePlayerItemHeldEvent();
        this.subscribeToBlockBreakEvent();
    }

    private void subscribePlayerItemHeldEvent() {
        Events.subscribe(PlayerItemHeldEvent.class)
                .handler(e -> {
                    ItemStack item = e.getPlayer().getInventory().getItem(e.getNewSlot());
                    if (item != null && this.plugin.getCore().isPickaxeSupported(item.getType()) && !this.plugin.getPickaxeLevelsManager().getPickaxeLevel(item).isPresent()) {
                        e.getPlayer().getInventory().setItem(e.getNewSlot(), this.plugin.getPickaxeLevelsManager().addDefaultPickaxeLevel(item, e.getPlayer()));
                    }
                }).bindWith(this.plugin.getCore());
    }

    private void subscribeToBlockBreakEvent() {
        Events.subscribe(BlockBreakEvent.class, EventPriority.HIGHEST)
                .filter(e -> !e.isCancelled())
                .filter(e -> e.getPlayer().getItemInHand() != null && this.plugin.getCore().isPickaxeSupported(e.getPlayer().getItemInHand().getType()))
                .handler(e -> {
                    ItemStack pickaxe = e.getPlayer().getItemInHand();
                    Player player = e.getPlayer();
                    this.plugin.getPickaxeLevelsManager().updatePickaxeLevel(player, pickaxe);
                }).bindWith(this.plugin.getCore());
    }
}
