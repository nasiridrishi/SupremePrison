package net.primegames.supremeprison.autosell.model;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class AutoSellItemStack {

    private final ItemStack itemStack;

    public AutoSellItemStack(ItemStack stack) {
        this.itemStack = stack;
    }

    public static AutoSellItemStack of(ItemStack item) {
        return new AutoSellItemStack(item);
    }

}
