package net.primegames.supremeprison.autosell.gui;

import net.primegames.supremeprison.autosell.SupremePrisonAutoSell;
import net.primegames.supremeprison.autosell.model.SellRegion;
import net.primegames.supremeprison.autosell.utils.SellPriceComparator;
import net.primegames.supremeprison.utils.compat.CompMaterial;
import net.primegames.supremeprison.utils.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class SellRegionGui extends Gui {

    private final SellRegion sellRegion;

    public SellRegionGui(SellRegion sellRegion, Player player) {
        super(player, 6, sellRegion.getRegion().getId() + " Prices");
        this.sellRegion = sellRegion;
    }

    @Override
    public void redraw() {
        this.clearItems();

        this.setActionItems();

        this.setBackItem();
    }

    private void setBackItem() {
        this.setItem(45, ItemStackBuilder.of(Material.ARROW).name("&cBack").lore("&7Click to go back to all regions").build(() -> {
            this.close();
            AllSellRegionsGui.createAndOpenTo(this.getPlayer());
        }));
    }

    private void setActionItems() {
        for (CompMaterial material : this.sellRegion.getSellingMaterialsSorted(new SellPriceComparator(sellRegion))) {
            this.addItemForMaterial(material);
        }
    }


    private void addItemForMaterial(CompMaterial material) {
        double price = this.sellRegion.getSellPriceForMaterial(material);

        this.addItem(ItemStackBuilder.of(material.toItem()).name(material.name()).lore(" ", String.format("&7Sell Price: &2$&a%,.2f", price), " ", "&aLeft-Click &7to edit the price", "&aRight-Click &7to remove.").build(() -> {
            this.deleteSellPrice(material);
            this.redraw();
        }, () -> {
            new UpdateSellPriceGui(this.getPlayer(), sellRegion, material).open();
        }));
    }

    private void deleteSellPrice(CompMaterial material) {
        this.sellRegion.removeSellPrice(material);
        SupremePrisonAutoSell.getInstance().getAutoSellConfig().saveSellRegion(sellRegion);
    }
}
