package net.primegames.supremeprison.history.gui.confirmation;

import net.primegames.supremeprison.history.SupremePrisonHistory;
import net.primegames.supremeprison.utils.gui.ConfirmationGui;
import net.primegames.supremeprison.utils.player.PlayerUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerClearHistoryConfirmationGUI extends ConfirmationGui {

    private final OfflinePlayer target;
    private final SupremePrisonHistory plugin;

    public PlayerClearHistoryConfirmationGUI(Player player, OfflinePlayer target, SupremePrisonHistory plugin) {
        super(player, "Clear " + target.getName() + "?");
        this.target = target;
        this.plugin = plugin;
    }

    @Override
    public void confirm(boolean confirm) {
        if (confirm) {
            this.plugin.getHistoryManager().clearPlayerHistory(this.target);
            PlayerUtils.sendMessage(this.getPlayer(), "&aYou have cleared history data of player &e" + target.getName());
        }
        this.close();
    }
}
