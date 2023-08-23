package net.primegames.supremeprison.autosell.api;

import net.primegames.supremeprison.autosell.SupremePrisonAutoSell;
import net.primegames.supremeprison.autosell.model.SellRegion;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

public final class SupremePrisonAutoSellAPIImpl implements SupremePrisonAutoSellAPI {

    private final SupremePrisonAutoSell plugin;

    public SupremePrisonAutoSellAPIImpl(SupremePrisonAutoSell plugin) {
        this.plugin = plugin;
    }

    @Override
    public double getCurrentEarnings(Player player) {
        return plugin.getManager().getCurrentEarnings(player);
    }

    @Override
    public double getPriceForItem(String regionName, ItemStack item) {
        return plugin.getManager().getPriceForItem(regionName, item);
    }

    @Override
    public double getPriceForBlock(Block block) {
        return plugin.getManager().getPriceForBlock(block);
    }

    @Override
    public void sellBlocks(Player player, List<Block> blocks) {
        plugin.getManager().sellBlocks(player, blocks);
    }

    @Override
    public boolean hasAutoSellEnabled(Player p) {
        return plugin.getManager().hasAutoSellEnabled(p);
    }

    @Override
    public Collection<SellRegion> getSellRegions() {
        return plugin.getManager().getAutoSellRegions();
    }

    @Override
    public SellRegion getSellRegionAtLocation(Location location) {
        return plugin.getManager().getAutoSellRegion(location);
    }
}
