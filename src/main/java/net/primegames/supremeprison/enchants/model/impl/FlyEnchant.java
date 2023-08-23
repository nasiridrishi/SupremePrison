package net.primegames.supremeprison.enchants.model.impl;

import net.primegames.supremeprison.enchants.SupremePrisonEnchants;
import net.primegames.supremeprison.enchants.model.SupremePrisonEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public final class FlyEnchant extends SupremePrisonEnchantment {


    public FlyEnchant(SupremePrisonEnchants instance) {
        super(instance, 8);
    }

    @Override
    public void onEquip(Player p, ItemStack pickAxe, int level) {
        p.setAllowFlight(true);

    }

    @Override
    public void onUnequip(Player p, ItemStack pickAxe, int level) {
        p.setAllowFlight(false);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int enchantLevel) {
    }

    @Override
    public double getChanceToTrigger(int enchantLevel) {
        return 100.0;
    }

    @Override
    public void reload() {
        super.reload();
    }

    @Override
    public String getAuthor() {
        return "Drawethree";
    }
}
