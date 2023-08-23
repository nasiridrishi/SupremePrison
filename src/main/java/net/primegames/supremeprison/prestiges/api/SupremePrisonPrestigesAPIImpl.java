package net.primegames.supremeprison.prestiges.api;

import net.primegames.supremeprison.prestiges.SupremePrisonPrestiges;
import net.primegames.supremeprison.prestiges.model.Prestige;
import org.bukkit.entity.Player;

public final class SupremePrisonPrestigesAPIImpl implements SupremePrisonPrestigesAPI {

    private final SupremePrisonPrestiges plugin;

    public SupremePrisonPrestigesAPIImpl(SupremePrisonPrestiges plugin) {
        this.plugin = plugin;
    }

    @Override
    public Prestige getPlayerPrestige(Player p) {
        return plugin.getPrestigeManager().getPlayerPrestige(p);
    }

    @Override
    public void setPlayerPrestige(Player player, Prestige prestige) {
        plugin.getPrestigeManager().setPlayerPrestige(null, player, prestige.getId());
    }

    @Override
    public void setPlayerPrestige(Player player, long prestige) {
        plugin.getPrestigeManager().setPlayerPrestige(null, player, prestige);

    }
}
