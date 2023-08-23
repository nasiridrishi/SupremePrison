package net.primegames.supremeprison.utils.gui;

import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.utils.player.PlayerUtils;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ClearDBGui extends ConfirmationGui {

    private final SupremePrisonModule module;

    public ClearDBGui(Player player, SupremePrisonModule module) {
        super(player, module == null ? "Clear all player data?" : "Clear data for " + module.getName() + "?");
        this.module = module;
    }

    @Override
    public void confirm(boolean confirm) {
        if (confirm) {
            if (this.module == null) {
                this.getAllModules().forEach(SupremePrisonModule::resetPlayerData);
                PlayerUtils.sendMessage(this.getPlayer(), "&aX-Prison - All Modules Data have been reset.");
            } else {
                this.module.resetPlayerData();
                PlayerUtils.sendMessage(this.getPlayer(), "&aX-Prison - DB Player data for module " + module.getName() + " has been reset.");
            }
        }
        this.close();
    }

    private Collection<SupremePrisonModule> getAllModules() {
        return SupremePrison.getInstance().getModules();
    }
}
