package net.primegames.supremeprison.enchants.gui;

import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.enchants.SupremePrisonEnchants;
import net.primegames.supremeprison.enchants.model.SupremePrisonEnchantment;
import net.primegames.supremeprison.enchants.utils.GuiUtils;
import net.primegames.supremeprison.utils.compat.CompMaterial;
import net.primegames.supremeprison.utils.item.ItemStackBuilder;
import net.primegames.supremeprison.utils.misc.SkullUtils;
import net.primegames.supremeprison.utils.text.TextUtils;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
public final class DisenchantGUI extends Gui {

    private static List<String> GUI_ITEM_LORE;
    private static String GUI_TITLE;
    private static Item EMPTY_SLOT_ITEM;
    private static Item HELP_ITEM;
    private static int HELP_ITEM_SLOT;
    private static int PICKAXE_ITEM_SLOT;
    private static int GUI_LINES;
    private static boolean PICKAXE_ITEM_ENABLED;
    private static boolean HELP_ITEM_ENABLED;
    private final int pickaxePlayerInventorySlot;
    private final SupremePrisonEnchants plugin;
    @Getter
    @Setter
    private ItemStack pickAxe;

    public DisenchantGUI(SupremePrisonEnchants plugin, Player player, ItemStack pickAxe, int pickaxePlayerInventorySlot) {
        super(player, GUI_LINES, GUI_TITLE);
        this.plugin = plugin;
        this.pickAxe = pickAxe;
        this.pickaxePlayerInventorySlot = pickaxePlayerInventorySlot;

        Events.subscribe(InventoryCloseEvent.class, EventPriority.LOWEST)
                .filter(e -> e.getInventory().equals(this.getHandle()))
                .handler(e -> {
                    SupremePrison.getInstance().getEnchants().getEnchantsManager().handlePickaxeUnequip(this.getPlayer(), this.pickAxe);
                    SupremePrison.getInstance().getEnchants().getEnchantsManager().handlePickaxeEquip(this.getPlayer(), this.pickAxe);
                }).bindWith(this);

        Schedulers.sync().runLater(() -> {
            if (!pickAxe.equals(this.getPlayer().getInventory().getItem(this.pickaxePlayerInventorySlot))) {
                this.close();
            }
        }, 10);
        setFallbackGui(player1 -> new EnchantGUI(plugin, player, pickAxe, pickaxePlayerInventorySlot));

    }

    public static void init() {

        GUI_ITEM_LORE = SupremePrisonEnchants.getInstance().getEnchantsConfig().getYamlConfig().getStringList("disenchant_menu.item.lore");
        GUI_TITLE = TextUtils.applyColor(SupremePrisonEnchants.getInstance().getEnchantsConfig().getYamlConfig().getString("disenchant_menu.title"));
        GUI_LINES = SupremePrisonEnchants.getInstance().getEnchantsConfig().getYamlConfig().getInt("disenchant_menu.lines");

        EMPTY_SLOT_ITEM = ItemStackBuilder.
                of(CompMaterial.fromString(SupremePrisonEnchants.getInstance().getEnchantsConfig().getYamlConfig().getString("disenchant_menu.empty_slots")).toItem()).name("&e").buildItem().build();

        HELP_ITEM_ENABLED = SupremePrisonEnchants.getInstance().getEnchantsConfig().getYamlConfig().getBoolean("disenchant_menu.help_item.enabled", true);
        PICKAXE_ITEM_ENABLED = SupremePrisonEnchants.getInstance().getEnchantsConfig().getYamlConfig().getBoolean("disenchant_menu.pickaxe_enabled", true);

        if (HELP_ITEM_ENABLED) {
            HELP_ITEM = ItemStackBuilder.of(SkullUtils.HELP_SKULL).name(Objects.requireNonNull(SupremePrisonEnchants.getInstance().getEnchantsConfig().getYamlConfig().getString("disenchant_menu.help_item.name"))).lore(SupremePrisonEnchants.getInstance().getEnchantsConfig().getYamlConfig().getStringList("disenchant_menu.help_item.lore")).buildItem().build();
            HELP_ITEM_SLOT = SupremePrisonEnchants.getInstance().getEnchantsConfig().getYamlConfig().getInt("disenchant_menu.help_item.slot");
        }

        if (PICKAXE_ITEM_ENABLED) {
            PICKAXE_ITEM_SLOT = SupremePrisonEnchants.getInstance().getEnchantsConfig().getYamlConfig().getInt("disenchant_menu.pickaxe_slot");
        }
    }

    @Override
    public void redraw() {

        if (isFirstDraw()) {
            for (int i = 0; i < this.getHandle().getSize(); i++) {
                this.setItem(i, EMPTY_SLOT_ITEM);
            }
        }


        if (HELP_ITEM_ENABLED) {
            this.setItem(HELP_ITEM_SLOT, HELP_ITEM);
        }

        if (PICKAXE_ITEM_ENABLED) {
            this.setItem(PICKAXE_ITEM_SLOT, Item.builder(pickAxe).build());
        }

        Collection<SupremePrisonEnchantment> allEnchants = this.plugin.getEnchantsRepository().getAll();

        for (SupremePrisonEnchantment enchantment : allEnchants) {

            if (!enchantment.isRefundEnabled() || !enchantment.isEnabled()) {
                continue;
            }

            int level = SupremePrisonEnchants.getInstance().getEnchantsManager().getEnchantLevel(this.pickAxe, enchantment);
            this.setItem(enchantment.getRefundGuiSlot(), getRefundGuiItem(enchantment, this, level));
        }
    }

    private Item getRefundGuiItem(SupremePrisonEnchantment enchantment, DisenchantGUI gui, int level) {
        Material m = enchantment.isRefundEnabled() ? enchantment.getMaterial() : CompMaterial.BARRIER.toMaterial();
        ItemStackBuilder builder = ItemStackBuilder.of(m);

        if (enchantment.getBase64() != null && !enchantment.getBase64().isEmpty()) {
            builder = ItemStackBuilder.of(SkullUtils.MONEY_SKULL);
        }

        builder.name(enchantment.isRefundEnabled() ? enchantment.getGuiName() : this.plugin.getEnchantsConfig().getMessage("enchant_cant_disenchant"));
        builder.lore(enchantment.isRefundEnabled() ? GuiUtils.translateGuiLore(enchantment, GUI_ITEM_LORE, level) : new ArrayList<>());

        return enchantment.isRefundEnabled() ? builder.buildItem().bind(handler -> {
            if (handler.getClick() == ClickType.MIDDLE || handler.getClick() == ClickType.SHIFT_RIGHT) {
                this.plugin.getEnchantsManager().disenchant(enchantment, gui, level, 100);
                gui.redraw();
            } else if (handler.getClick() == ClickType.LEFT) {
                this.plugin.getEnchantsManager().disenchant(enchantment, gui, level, 1);
                gui.redraw();
            } else if (handler.getClick() == ClickType.RIGHT) {
                this.plugin.getEnchantsManager().disenchant(enchantment, gui, level, 10);
                gui.redraw();
            } else if (handler.getClick() == ClickType.DROP) {
                this.plugin.getEnchantsManager().disenchantMax(enchantment, gui, level);
            }
        }, ClickType.MIDDLE, ClickType.SHIFT_RIGHT, ClickType.LEFT, ClickType.RIGHT, ClickType.DROP).build() : builder.buildConsumer(handler -> handler.getWhoClicked().sendMessage(this.plugin.getEnchantsConfig().getMessage("enchant_cant_disenchant")));
    }
}
