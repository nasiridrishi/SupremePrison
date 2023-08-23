package net.primegames.supremeprison.gangs.gui.panel;

import net.primegames.supremeprison.gangs.SupremePrisonGangs;
import net.primegames.supremeprison.gangs.model.Gang;
import net.primegames.supremeprison.utils.gui.ConfirmationGui;
import org.bukkit.entity.Player;

public final class DisbandGangGUI extends ConfirmationGui {

    private final SupremePrisonGangs plugin;
    private final Gang gang;

    public DisbandGangGUI(SupremePrisonGangs plugin, Player player, Gang gang) {
        super(player, plugin.getConfig().getGangDisbandGUITitle());
        this.plugin = plugin;
        this.gang = gang;
    }

    @Override
    public void confirm(boolean confirm) {
        if (confirm) {
            this.plugin.getGangsManager().disbandGang(getPlayer(), this.gang, false);
        }
        this.close();
    }
}
