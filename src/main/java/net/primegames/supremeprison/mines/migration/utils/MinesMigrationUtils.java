package net.primegames.supremeprison.mines.migration.utils;

import net.primegames.supremeprison.mines.migration.gui.AllMinesMigrationGui;
import net.primegames.supremeprison.mines.migration.gui.MinesMigrationGui;
import net.primegames.supremeprison.mines.migration.model.MinesMigration;
import org.bukkit.entity.Player;

public class MinesMigrationUtils {

    private MinesMigrationUtils() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    public static void openMinesMigrationGui(Player player, MinesMigration migration) {
        MinesMigrationGui gui = new MinesMigrationGui(player, migration);
        gui.open();
    }

    public static void openAllMinesMigrationGui(Player player) {
        AllMinesMigrationGui gui = new AllMinesMigrationGui(player);
        gui.open();
    }
}
