package net.primegames.supremeprison.gangs.api;

import net.primegames.supremeprison.gangs.managers.GangsManager;
import net.primegames.supremeprison.gangs.model.Gang;
import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.Optional;

public final class SupremePrisonGangsAPIImpl implements SupremePrisonGangsAPI {

    private final GangsManager gangsManager;

    public SupremePrisonGangsAPIImpl(GangsManager gangsManager) {
        this.gangsManager = gangsManager;
    }

    @Override
    public Optional<Gang> getPlayerGang(OfflinePlayer player) {
        return this.gangsManager.getPlayerGang(player);
    }

    @Override
    public Optional<Gang> getByName(String name) {
        return this.gangsManager.getGangWithName(name);
    }

    @Override
    public Collection<Gang> getAllGangs() {
        return this.gangsManager.getAllGangs();
    }
}
