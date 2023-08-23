package net.primegames.supremeprison.enchants.api;

import net.primegames.supremeprison.enchants.model.SupremePrisonEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface SupremePrisonEnchantsAPI {


    /**
     * Method to get all custom enchants applied on specific ItemStack
     *
     * @param itemStack ItemStack
     * @return
     */
    Map<SupremePrisonEnchantment, Integer> getEnchants(ItemStack itemStack);

    /**
     * Method to check if item has specific enchant
     *
     * @param item        {@link ItemStack}
     * @param enchantment {@link SupremePrisonEnchantment}
     * @return true if item has enchant
     */
    boolean hasEnchant(ItemStack item, SupremePrisonEnchantment enchantment);

    /**
     * Method to get enchant level of specific ItemStack
     *
     * @param item        ItemStack
     * @param enchantment {@link SupremePrisonEnchantment}
     * @return 0 if enchant was not found, otherwise level of enchant
     */
    int getEnchantLevel(ItemStack item, SupremePrisonEnchantment enchantment);

    /**
     * Method to set enchant with specific level to pickaxe
     *
     * @param item        pickaxe
     * @param enchantment {@link SupremePrisonEnchantment}
     * @param level       Enchant Level
     * @return modified ItemStack
     */
    ItemStack setEnchantLevel(Player player, ItemStack item, SupremePrisonEnchantment enchantment, int level);

    /**
     * Method to remove enchant from pickaxe
     *
     * @param item        ItemStack pickaxe
     * @param enchantment {@link SupremePrisonEnchantment}
     * @return modified ItemStack
     */
    ItemStack removeEnchant(Player player, ItemStack item, SupremePrisonEnchantment enchantment);

    /**
     * Method to get Enchant by ID
     *
     * @param id enchant id
     * @return SupremePrisonEnchantment
     */
    SupremePrisonEnchantment getById(int id);

    /**
     * Method to get Enchant by ID
     *
     * @param rawName enchant rawname
     * @return SupremePrisonEnchantment
     */
    SupremePrisonEnchantment getByName(String rawName);

    /**
     * Registers a specific {@link SupremePrisonEnchantment}
     *
     * @param enchantment
     * @return
     */
    boolean registerEnchant(SupremePrisonEnchantment enchantment);

    /**
     * Unregisters a specific {@link SupremePrisonEnchantment}
     *
     * @param enchantment
     * @return
     */
    boolean unregisterEnchant(SupremePrisonEnchantment enchantment);

}
