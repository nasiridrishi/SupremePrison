package net.primegames.supremeprison.history.manager;

import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.autominer.SupremePrisonAutoMiner;
import net.primegames.supremeprison.gangs.SupremePrisonGangs;
import net.primegames.supremeprison.history.SupremePrisonHistory;
import net.primegames.supremeprison.history.model.HistoryLine;
import net.primegames.supremeprison.multipliers.SupremePrisonMultipliers;
import net.primegames.supremeprison.prestiges.SupremePrisonPrestiges;
import net.primegames.supremeprison.ranks.SupremePrisonRanks;
import net.primegames.supremeprison.utils.compat.CompMaterial;
import net.primegames.supremeprison.utils.item.ItemStackBuilder;
import net.primegames.supremeprison.utils.misc.SkullUtils;
import me.lucko.helper.menu.Item;
import me.lucko.helper.menu.paginated.PaginatedGuiBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HistoryManager {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    private final SupremePrisonHistory plugin;

    public HistoryManager(SupremePrisonHistory plugin) {
        this.plugin = plugin;
    }

    public List<HistoryLine> getPlayerHistory(OfflinePlayer player) {
        return this.plugin.getHistoryService().getPlayerHistory(player);
    }

    public void createPlayerHistoryLine(OfflinePlayer player, SupremePrisonModule module, String context) {
        HistoryLine history = createHistoryLineObject(player, module, context);
        this.plugin.getHistoryService().createHistoryLine(player, history);
    }

    public void clearPlayerHistory(OfflinePlayer target) {
        this.plugin.getHistoryService().deleteHistory(target);
    }

    public void openPlayerHistoryGui(Player sender, OfflinePlayer target, Predicate<HistoryLine> filter) {
        PaginatedGuiBuilder builder = PaginatedGuiBuilder.create();
        builder.lines(6);
        builder.title("History: " + target.getName());
        builder.nextPageSlot(53);
        builder.previousPageSlot(45);
        builder.build(sender, gui -> {
            Stream<HistoryLine> historyLinesStream = getPlayerHistory(target).stream();
            if (filter != null) {
                historyLinesStream = historyLinesStream.filter(filter);
            }
            List<HistoryLine> historyLines = historyLinesStream.sorted(Comparator.comparing(HistoryLine::getCreatedAt).reversed()).collect(Collectors.toList());
            if (historyLines.isEmpty()) {
                return Collections.singletonList(getEmptyHistoryItem());
            } else {
                return historyLines.stream().map(this::createHistoryLineGuiItem).collect(Collectors.toList());
            }
        }).open();
    }

    private Item getEmptyHistoryItem() {
        return ItemStackBuilder.of(CompMaterial.BARRIER.toItem()).name("&4&lNo History").lore("&cNo history is present for this player.").buildItem().build();
    }

    private HistoryLine createHistoryLineObject(OfflinePlayer player, SupremePrisonModule module, String context) {
        Validate.notNull(player, "Player cannot be null!");
        Validate.notNull(module, "Module cannot be null!");
        Validate.notNull(context, "Context cannot be null!");

        HistoryLine history = new HistoryLine();
        history.setCreatedAt(new Date());
        history.setContext(context);
        history.setPlayerUuid(player.getUniqueId());
        history.setModule(module.getName());
        history.setUuid(UUID.randomUUID());
        return history;
    }

    private Item createHistoryLineGuiItem(HistoryLine line) {
        return ItemStackBuilder
                .of(getIconForModule(line.getModule()))
                .name("&e" + line.getModule())
                .lore(
                        " ",
                        "&7Module: &e" + line.getModule(),
                        "&7Date: &e" + DATE_FORMAT.format(line.getCreatedAt()),
                        "&7Context:",
                        "&f" + line.getContext(),
                        " ").buildItem().build();
    }

    private ItemStack getIconForModule(String moduleName) {
        switch (moduleName) {
            case SupremePrisonMultipliers.MODULE_NAME:
                return SkullUtils.COIN_SKULL.clone();
            case SupremePrisonGangs.MODULE_NAME:
                return SkullUtils.GANG_SKULL.clone();
            case SupremePrisonPrestiges
                    .MODULE_NAME:
                return SkullUtils.DIAMOND_P_SKULL.clone();
            case SupremePrisonRanks
                    .MODULE_NAME:
                return SkullUtils.DIAMOND_R_SKULL.clone();
            case SupremePrisonAutoMiner
                    .MODULE_NAME:
                return ItemStackBuilder.of(CompMaterial.DIAMOND_PICKAXE.toItem()).build();
            default:
                return ItemStackBuilder.of(CompMaterial.BOOK.toItem()).build();
        }

    }
}
