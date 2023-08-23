package net.primegames.supremeprison.enchants.model.impl;

import net.primegames.supremeprison.enchants.SupremePrisonEnchants;
import net.primegames.supremeprison.enchants.model.SupremePrisonEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class NightVisionEnchant extends SupremePrisonEnchantment {
    public NightVisionEnchant(SupremePrisonEnchants instance) {
        super(instance, 7);
    }

    @Override
    public void onEquip(Player p, ItemStack pickAxe, int level) {
        if (level == 0) {
            this.onUnequip(p, pickAxe, level);
            return;
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, level - 1, true, true), true);
    }

    @Override
    public void onUnequip(Player p, ItemStack pickAxe, int level) {
        p.removePotionEffect(PotionEffectType.NIGHT_VISION);
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
