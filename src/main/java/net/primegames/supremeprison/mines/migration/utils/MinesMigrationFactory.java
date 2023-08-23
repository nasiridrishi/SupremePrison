package net.primegames.supremeprison.mines.migration.utils;

import net.primegames.supremeprison.mines.migration.exception.MinesMigrationNotSupportedException;
import net.primegames.supremeprison.mines.migration.model.MinesMigration;
import net.primegames.supremeprison.mines.migration.model.impl.MineResetLiteMigration;

public class MinesMigrationFactory {

    public static MinesMigration fromPlugin(String pluginName) throws MinesMigrationNotSupportedException {

        if ("mineresetlite".equalsIgnoreCase(pluginName)) {
            return new MineResetLiteMigration();
        }
        throw new MinesMigrationNotSupportedException(pluginName);

    }
}
