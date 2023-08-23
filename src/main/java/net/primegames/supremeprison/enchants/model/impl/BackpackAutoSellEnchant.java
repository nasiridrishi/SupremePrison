package net.primegames.supremeprison.enchants.model.impl;

import net.primegames.supremeprison.enchants.SupremePrisonEnchants;
import net.primegames.supremeprison.enchants.model.SupremePrisonEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public final class BackpackAutoSellEnchant extends SupremePrisonEnchantment {

    private double chance;

    public BackpackAutoSellEnchant(SupremePrisonEnchants instance) {
        super(instance, 19);
        this.chance = plugin.getEnchantsConfig().getYamlConfig().getDouble("enchants." + id + ".Chance");
    }

    @Override
    public String getAuthor() {
        return "Drawethree";
    }

    @Override
    public void onEquip(Player p, ItemStack pickAxe, int level) {

    }

    @Override
    public void onUnequip(Player p, ItemStack pickAxe, int level) {

    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int enchantLevel) {

//        if (!SupremePrison.getInstance().isUltraBackpacksEnabled()) {
//            return;
//        }
//
//        double chance = getChanceToTrigger(enchantLevel);
//
//        if (chance < ThreadLocalRandom.current().nextDouble(100)) {
//            return;
//        }

//        try {
//            UltraBackpacksAPI.sellBackpack(e.getPlayer(), true);
//        } catch (BackpackNotFoundException ignored) {
//            this.plugin.getCore().debug("BackpackAutoSellEnchant::onBlockBreak > Player " + e.getPlayer().getName() + " does not have backpack.", this.plugin);
//        }

    }

    @Override
    public double getChanceToTrigger(int enchantLevel) {
        return this.chance * enchantLevel;
    }

    @Override
    public void reload() {
        super.reload();
        this.chance = plugin.getEnchantsConfig().getYamlConfig().getDouble("enchants." + id + ".Chance");
    }
}
