package net.primegames.supremeprison.pickaxelevels.manager;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.pickaxelevels.SupremePrisonPickaxeLevels;
import net.primegames.supremeprison.pickaxelevels.model.PickaxeLevel;
import net.primegames.supremeprison.utils.item.ItemStackBuilder;
import net.primegames.supremeprison.utils.misc.ProgressBar;
import net.primegames.supremeprison.utils.player.PlayerUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import me.lucko.helper.Schedulers;
import net.primegames.supremeprison.pickaxelevels.utils.PickaxeLevelsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PickaxeLevelsManager {

    private final SupremePrisonPickaxeLevels plugin;

    public PickaxeLevelsManager(SupremePrisonPickaxeLevels plugin) {
        this.plugin = plugin;
    }

    public Optional<PickaxeLevel> getNextPickaxeLevel(PickaxeLevel currentLevel) {
        if (currentLevel == null || currentLevel == getMaxLevel()) {
            return Optional.empty();
        }
        return this.getPickaxeLevel(currentLevel.getLevel() + 1);
    }

    private PickaxeLevel getMaxLevel() {
        return this.plugin.getPickaxeLevelsConfig().getMaxLevel();
    }


    public Optional<PickaxeLevel> getPickaxeLevel(int level) {
        return this.plugin.getPickaxeLevelsConfig().getPickaxeLevel(level);
    }

    public Optional<PickaxeLevel> getPickaxeLevel(ItemStack itemStack) {
        if (itemStack == null || !this.plugin.getCore().isPickaxeSupported(itemStack.getType())) {
            return Optional.empty();
        }

        NBTItem nbtItem = new NBTItem(itemStack);

        if (!nbtItem.hasKey(PickaxeLevelsUtils.NBT_TAG_INDETIFIER)) {
            return Optional.of(getDefaultLevel());
        }

        return this.getPickaxeLevel(nbtItem.getInteger(PickaxeLevelsUtils.NBT_TAG_INDETIFIER));
    }

    private PickaxeLevel getDefaultLevel() {
        return this.plugin.getPickaxeLevelsConfig().getDefaultLevel();
    }

    public ItemStack setPickaxeLevel(ItemStack item, PickaxeLevel level, Player p) {

        if (level == null || level.getLevel() <= 0 || level.getLevel() > this.getMaxLevel().getLevel()) {
            return item;
        }

        NBTItem nbtItem = new NBTItem(item);

        if (!nbtItem.hasKey(PickaxeLevelsUtils.NBT_TAG_INDETIFIER)) {
            nbtItem.setInteger(PickaxeLevelsUtils.NBT_TAG_INDETIFIER, 0);
        }

        nbtItem.setInteger(PickaxeLevelsUtils.NBT_TAG_INDETIFIER, level.getLevel());

        ItemStackBuilder builder = ItemStackBuilder.of(nbtItem.getItem());
        if (level.getDisplayName() != null && !level.getDisplayName().isEmpty()) {
            builder = builder.name(this.getDisplayName(level, p));
        }

        item = builder.build();
        item = this.updatePickaxe(p, item);
        return item;
    }

    private ItemStack updatePickaxe(Player p, ItemStack item) {
        return this.plugin.getCore().getEnchants().getEnchantsManager().updatePickaxe(p, item);
    }

    public ItemStack addDefaultPickaxeLevel(ItemStack item, Player p) {
        return setPickaxeLevel(item, this.getDefaultLevel(), p);
    }


    public ItemStack findPickaxe(Player p) {
        for (ItemStack i : p.getInventory()) {
            if (i == null) {
                continue;
            }
            if (this.plugin.getCore().isPickaxeSupported(i.getType())) {
                return i;
            }
        }
        return null;
    }

    public String getProgressBar(Player player) {
        ItemStack pickaxe = findPickaxe(player);
        return this.getProgressBar(pickaxe);
    }

    public String getProgressBar(ItemStack item) {

        Optional<PickaxeLevel> currentLevelOptional = this.getPickaxeLevel(item);

        double current = 0;
        double required = 1;

        if (currentLevelOptional.isPresent()) {
            PickaxeLevel currentLevel = currentLevelOptional.get();
            Optional<PickaxeLevel> nextLevelOptional = this.getNextPickaxeLevel(currentLevel);
            current = this.getBlocksBroken(item) - currentLevel.getBlocksRequired();
            if (nextLevelOptional.isPresent()) {
                PickaxeLevel nextLevel = nextLevelOptional.get();
                required = nextLevel.getBlocksRequired() - currentLevel.getBlocksRequired();
            }
        }
        return ProgressBar.getProgressBar(this.getProgressBarLength(), this.getProgressBarDelimiter(), current, required);
    }

    public long getBlocksBroken(ItemStack item) {

        if (item == null || item.getType() == Material.AIR) {
            return 0;
        }

        NBTItem nbtItem = new NBTItem(item);

        if (!nbtItem.hasKey("blocks-broken")) {
            return 0;
        }

        return nbtItem.getLong("blocks-broken");
    }

    private String getProgressBarDelimiter() {
        return this.plugin.getPickaxeLevelsConfig().getProgressBarDelimiter();
    }

    private int getProgressBarLength() {
        return this.plugin.getPickaxeLevelsConfig().getProgressBarLength();
    }

    public void giveRewards(PickaxeLevel level, Player p) {
        if (!Bukkit.isPrimaryThread()) {
            Schedulers.sync().run(() -> level.getRewards().forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", p.getName()))));
        } else {
            level.getRewards().forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", p.getName())));
        }
    }

    public String getDisplayName(PickaxeLevel level, Player p) {
        if (SupremePrison.getInstance().isPlaceholderAPIEnabled()) {
            return PlaceholderAPI.setPlaceholders(p, level.getDisplayName()).replace("%player%", p.getName());
        }
        return level.getDisplayName().replace("%player%", p.getName());
    }

    public Optional<PickaxeLevel> getPickaxeLevel(Player player) {
        ItemStack item = this.findPickaxe(player);
        return this.getPickaxeLevel(item);
    }

    public void updatePickaxeLevel(Player player, ItemStack pickaxe) {
        long currentBlocks = this.plugin.getPickaxeLevelsManager().getBlocksBroken(pickaxe);

        Optional<PickaxeLevel> currentLevelOptional = this.getPickaxeLevel(pickaxe);

        if (!currentLevelOptional.isPresent()) {
            return;
        }

        PickaxeLevel currentLevel = currentLevelOptional.get();
        Optional<PickaxeLevel> nextLevelOptional = this.getNextPickaxeLevel(currentLevel);

        List<PickaxeLevel> toGive = new ArrayList<>();

        while (nextLevelOptional.isPresent()) {
            PickaxeLevel nextLevel = nextLevelOptional.get();
            if (currentBlocks < nextLevel.getBlocksRequired()) {
                break;
            }
            toGive.add(nextLevel);
            nextLevelOptional = this.getNextPickaxeLevel(nextLevel);
        }

        if (!toGive.isEmpty()) {
            toGive.forEach(pickaxeLevel -> this.giveRewards(pickaxeLevel, player));
            ItemStack updatedPickaxe = this.setPickaxeLevel(pickaxe, toGive.get(toGive.size() - 1), player);
            player.setItemInHand(updatedPickaxe);
            PlayerUtils.sendMessage(player, this.plugin.getPickaxeLevelsConfig().getMessage("pickaxe-level-up").replace("%level%", String.valueOf(toGive.get(toGive.size() - 1).getLevel())));
        }
    }
}
