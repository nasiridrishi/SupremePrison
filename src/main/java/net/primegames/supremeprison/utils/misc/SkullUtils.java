package net.primegames.supremeprison.utils.misc;

import net.primegames.supremeprison.utils.compat.CompMaterial;
import net.primegames.supremeprison.utils.compat.MinecraftVersion;
import net.primegames.supremeprison.utils.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class SkullUtils {

    public static final ItemStack HELP_SKULL = ItemStackBuilder.of(Material.COMMAND_BLOCK_MINECART).build();
    public static final ItemStack DIAMOND_R_SKULL = ItemStackBuilder.of(Material.DIAMOND_PICKAXE).build();
    public static final ItemStack DIAMOND_P_SKULL = ItemStackBuilder.of(Material.DIAMOND_PICKAXE).build();
    public static final ItemStack MONEY_SKULL = ItemStackBuilder.of(Material.SUNFLOWER).build();
    public static final ItemStack COIN_SKULL = ItemStackBuilder.of(Material.SUNFLOWER).build();
    public static final ItemStack GANG_SKULL = ItemStackBuilder.of(Material.ZOMBIE_HEAD).build();
    public static final ItemStack INFO_SKULL = ItemStackBuilder.of(Material.PLAYER_HEAD).build();
    public static final ItemStack COMMAND_BLOCK_SKULL = ItemStackBuilder.of(Material.COMMAND_BLOCK).build();
    public static final ItemStack CHECK_SKULL = ItemStackBuilder.of(Material.GREEN_WOOL).build();
    public static final ItemStack CROSS_SKULL = ItemStackBuilder.of(Material.BARREL).build();
    public static final ItemStack DANGER_SKULL = ItemStackBuilder.of(Material.BARREL).build();


    private SkullUtils() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    public static void init() {
        //nothing here, just to make sure the class gets loaded on start.
    }

    public static ItemStack createPlayerHead(OfflinePlayer player, String displayName, List<String> lore) {
        ItemStack baseItem = CompMaterial.PLAYER_HEAD.toItem();
        SkullMeta meta = (SkullMeta) baseItem.getItemMeta();

        if (MinecraftVersion.atLeast(MinecraftVersion.V.v1_13)) {
            meta.setOwningPlayer(player);
        } else {
            meta.setOwner(player.getName());
        }
        baseItem.setItemMeta(meta);
        return ItemStackBuilder.of(baseItem).name(displayName).lore(lore).build();
    }
}
