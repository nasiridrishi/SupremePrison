package net.primegames.supremeprison.enchants.model.impl;

import net.primegames.supremeprison.enchants.SupremePrisonEnchants;
import net.primegames.supremeprison.enchants.model.SupremePrisonEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class XPSeekerEnchantment extends SupremePrisonEnchantment {


    public XPSeekerEnchantment(SupremePrisonEnchants plugin, int id) {
        super(plugin, id);
    }

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public void onEquip(Player p, ItemStack pickAxe, int level) {

    }

    @Override
    public void onUnequip(Player p, ItemStack pickAxe, int level) {

    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int enchantLevel) {

    }

    @Override
    public double getChanceToTrigger(int enchantLevel) {
        return 0;
    }
}
