package net.primegames.supremeprison.gangs.gui.admin;

import net.primegames.supremeprison.gangs.SupremePrisonGangs;
import net.primegames.supremeprison.gangs.model.Gang;
import net.primegames.supremeprison.utils.gui.ConfirmationGui;
import org.bukkit.entity.Player;

public final class DisbandGangAdminGUI extends ConfirmationGui {

    private final SupremePrisonGangs plugin;
    private final Gang gang;

    public DisbandGangAdminGUI(SupremePrisonGangs plugin, Player player, Gang gang) {
        super(player, "Disband " + gang.getName() + " gang ?");
        this.plugin = plugin;
        this.gang = gang;
    }

    @Override
    public void confirm(boolean confirm) {
        if (confirm) {
            this.plugin.getGangsManager().disbandGang(this.getPlayer(), this.gang, true);
        }
        this.close();
    }
}
