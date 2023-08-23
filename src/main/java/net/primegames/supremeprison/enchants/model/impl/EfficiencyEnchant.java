package net.primegames.supremeprison.enchants.model.impl;

import net.primegames.supremeprison.enchants.SupremePrisonEnchants;
import net.primegames.supremeprison.enchants.model.SupremePrisonEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class EfficiencyEnchant extends SupremePrisonEnchantment {
    public EfficiencyEnchant(SupremePrisonEnchants instance) {
        super(instance, 1);
    }

    @Override
    public void onEquip(Player p, ItemStack pickAxe, int level) {
        ItemMeta meta = pickAxe.getItemMeta();
        assert meta != null;
        meta.addEnchant(Enchantment.DIG_SPEED, level, true);
        pickAxe.setItemMeta(meta);
    }

    @Override
    public void onUnequip(Player p, ItemStack pickAxe, int level) {

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
