package net.primegames.supremeprison.enchants.model.impl;

import net.primegames.supremeprison.enchants.SupremePrisonEnchants;
import net.primegames.supremeprison.enchants.model.SupremePrisonEnchantment;
import net.primegames.supremeprison.utils.compat.CompMaterial;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class FortuneEnchant extends SupremePrisonEnchantment {

    private static List<CompMaterial> blackListedBlocks;

    public FortuneEnchant(SupremePrisonEnchants instance) {
        super(instance, 3);
        blackListedBlocks = plugin.getEnchantsConfig().getYamlConfig().getStringList("enchants." + id + ".Blacklist").stream().map(CompMaterial::fromString).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static boolean isBlockBlacklisted(Block block) {
        CompMaterial blockMaterial = CompMaterial.fromBlock(block);
        return blackListedBlocks.contains(blockMaterial);
    }

    @Override
    public void onEquip(Player p, ItemStack pickAxe, int level) {
        ItemMeta meta = pickAxe.getItemMeta();
        assert meta != null;
        meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, level, true);
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
        blackListedBlocks = plugin.getEnchantsConfig().getYamlConfig().getStringList("enchants." + id + ".Blacklist").stream().map(CompMaterial::fromString).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public String getAuthor() {
        return "Drawethree";
    }
}
