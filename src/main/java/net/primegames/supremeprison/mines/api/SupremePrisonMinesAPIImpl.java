package net.primegames.supremeprison.mines.api;

import net.primegames.supremeprison.mines.SupremePrisonMines;
import net.primegames.supremeprison.mines.model.mine.Mine;
import org.bukkit.Location;

public final class SupremePrisonMinesAPIImpl implements SupremePrisonMinesAPI {

    private final SupremePrisonMines plugin;

    public SupremePrisonMinesAPIImpl(SupremePrisonMines plugin) {
        this.plugin = plugin;
    }

    @Override
    public Mine getMineByName(String name) {
        return this.plugin.getManager().getMineByName(name);
    }

    @Override
    public Mine getMineAtLocation(Location loc) {
        return this.plugin.getManager().getMineAtLocation(loc);
    }
}
