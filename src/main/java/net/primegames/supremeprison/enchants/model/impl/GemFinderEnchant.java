package net.primegames.supremeprison.enchants.model.impl;

import net.primegames.supremeprison.economy.PrisonCurrency;
import net.primegames.supremeprison.economy.PrisonEconomyManager;
import net.primegames.supremeprison.enchants.SupremePrisonEnchants;
import net.primegames.supremeprison.enchants.model.SupremePrisonEnchantment;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public final class GemFinderEnchant extends SupremePrisonEnchantment {

    private double chance;
    private String amountToGiveExpression;

    public GemFinderEnchant(SupremePrisonEnchants instance) {
        super(instance, 22);
        this.chance = plugin.getEnchantsConfig().getYamlConfig().getDouble("enchants." + id + ".Chance");
        this.amountToGiveExpression = plugin.getEnchantsConfig().getYamlConfig().getString("enchants." + id + ".Amount-To-Give");

    }

    @Override
    public void onEquip(Player p, ItemStack pickAxe, int level) {

    }

    @Override
    public void onUnequip(Player p, ItemStack pickAxe, int level) {

    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int enchantLevel) {
        double chance = getChanceToTrigger(enchantLevel);
        if (chance < ThreadLocalRandom.current().nextDouble(100)) {
            return;
        }
        long amount = (long) createExpression(enchantLevel).evaluate();
        Location location = e.getBlock().getLocation().add(ThreadLocalRandom.current().nextDouble(1), ThreadLocalRandom.current().nextDouble(1), ThreadLocalRandom.current().nextDouble(1));
        PrisonEconomyManager.getInstance().dropBalanceAsItem(e.getBlock().getWorld(), location, PrisonCurrency.GEM, amount);
    }

    @Override
    public double getChanceToTrigger(int enchantLevel) {
        return chance * enchantLevel;
    }

    @Override
    public void reload() {
        super.reload();
        this.amountToGiveExpression = plugin.getEnchantsConfig().getYamlConfig().getString("enchants." + id + ".Amount-To-Give");
        this.chance = plugin.getEnchantsConfig().getYamlConfig().getDouble("enchants." + id + ".Chance");
    }

    private Expression createExpression(int level) {
        return new ExpressionBuilder(this.amountToGiveExpression)
                .variables("level")
                .build()
                .setVariable("level", level);
    }

    @Override
    public String getAuthor() {
        return "Drawethree";
    }
}
