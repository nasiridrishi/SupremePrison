package net.primegames.supremeprison.mainmenu.confirmation;

import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.utils.gui.ConfirmationGui;
import net.primegames.supremeprison.utils.player.PlayerUtils;
import org.bukkit.entity.Player;

public class ResetModulePlayerDataConfirmationGui extends ConfirmationGui {
    private final SupremePrisonModule module;

    public ResetModulePlayerDataConfirmationGui(Player player, SupremePrisonModule module) {
        super(player, module == null ? "Reset all player data ?" : "Reset " + module.getName() + " player data?");
        this.module = module;
    }

    @Override
    public void confirm(boolean confirm) {
        if (confirm) {
            if (module == null) {
                SupremePrison.getInstance().getModules().forEach((module) -> {
                            if (module.isEnabled()) {
                                module.resetPlayerData();
                            }
                        }
                );
                PlayerUtils.sendMessage(this.getPlayer(), "&aSuccessfully reset player data of all modules.");
            } else {
                module.resetPlayerData();
                PlayerUtils.sendMessage(this.getPlayer(), "&aSuccessfully reset player data of &e&l" + this.module.getName() + " &amodule.");
            }

        }
        this.close();
    }
}
