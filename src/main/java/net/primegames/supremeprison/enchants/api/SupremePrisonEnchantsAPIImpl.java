package net.primegames.supremeprison.enchants.api;

import net.primegames.supremeprison.enchants.managers.EnchantsManager;
import net.primegames.supremeprison.enchants.model.SupremePrisonEnchantment;
import net.primegames.supremeprison.enchants.repo.EnchantsRepository;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public final class SupremePrisonEnchantsAPIImpl implements SupremePrisonEnchantsAPI {

    private final EnchantsManager enchantsManager;
    private final EnchantsRepository enchantsRepository;

    public SupremePrisonEnchantsAPIImpl(EnchantsManager enchantsManager, EnchantsRepository enchantsRepository) {
        this.enchantsManager = enchantsManager;
        this.enchantsRepository = enchantsRepository;
    }

    @Override
    public Map<SupremePrisonEnchantment, Integer> getEnchants(ItemStack pickAxe) {
        return this.enchantsManager.getItemEnchants(pickAxe);
    }

    @Override
    public boolean hasEnchant(ItemStack item, SupremePrisonEnchantment enchant) {
        return getEnchantLevel(item, enchant) != 0;
    }

    @Override
    public int getEnchantLevel(ItemStack item, SupremePrisonEnchantment enchantment) {
        return this.enchantsManager.getEnchantLevel(item, enchantment);
    }

    @Override
    public ItemStack setEnchantLevel(Player player, ItemStack item, SupremePrisonEnchantment enchantment, int level) {
        return this.enchantsManager.setEnchantLevel(player, item, enchantment, level);
    }

    @Override
    public ItemStack removeEnchant(Player player, ItemStack item, SupremePrisonEnchantment enchantment) {
        return this.enchantsManager.removeEnchant(player, item, enchantment);
    }

    @Override
    public SupremePrisonEnchantment getById(int id) {
        return this.enchantsRepository.getEnchantById(id);
    }

    @Override
    public SupremePrisonEnchantment getByName(String rawName) {
        return this.enchantsRepository.getEnchantByName(rawName);
    }

    @Override
    public boolean registerEnchant(SupremePrisonEnchantment enchantment) {
        return this.enchantsRepository.register(enchantment);
    }

    @Override
    public boolean unregisterEnchant(SupremePrisonEnchantment enchantment) {
        return this.enchantsRepository.unregister(enchantment);
    }
}
