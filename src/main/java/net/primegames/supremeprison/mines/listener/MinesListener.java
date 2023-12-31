package net.primegames.supremeprison.mines.listener;

import net.primegames.supremeprison.mines.SupremePrisonMines;
import net.primegames.supremeprison.mines.managers.MineManager;
import net.primegames.supremeprison.mines.model.mine.Mine;
import me.lucko.helper.Events;
import me.lucko.helper.serialize.Position;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;
import java.util.Objects;

public class MinesListener {

    private final SupremePrisonMines plugin;

    public MinesListener(SupremePrisonMines plugin) {
        this.plugin = plugin;
    }

    public void register() {
        this.subscribeToBlockBreakEvent();
        this.subscribeToPlayerInteractEvent();
    }

    private void subscribeToBlockBreakEvent() {
        Events.subscribe(BlockBreakEvent.class, EventPriority.HIGHEST)
                .filter(e -> !e.isCancelled())
                .handler(e -> {
                    Mine mine = this.plugin.getManager().getMineAtLocation(e.getBlock().getLocation());

                    if (mine == null) {
                        return;
                    }

                    if (mine.isResetting()) {
                        e.setCancelled(true);
                        return;
                    }

                    mine.handleBlockBreak(Collections.singletonList(e.getBlock()));
                }).bindWith(this.plugin.getCore());
    }

    private void subscribeToPlayerInteractEvent() {
        Events.subscribe(PlayerInteractEvent.class)
                .filter(e -> e.getItem() != null && e.getItem().isSimilar(MineManager.SELECTION_TOOL) && e.getClickedBlock() != null)
                .handler(e -> {
                    int pos = e.getAction() == Action.LEFT_CLICK_BLOCK ? 1 : e.getAction() == Action.RIGHT_CLICK_BLOCK ? 2 : -1;

                    if (pos == -1) {
                        return;
                    }

                    e.setCancelled(true);

                    this.plugin.getManager().selectPosition(e.getPlayer(), pos, Position.of(Objects.requireNonNull(e.getClickedBlock())));
                }).bindWith(this.plugin.getCore());
    }
}
