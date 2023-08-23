package net.primegames.supremeprison.mainmenu.confirmation;

import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.utils.gui.ConfirmationGui;
import net.primegames.supremeprison.utils.player.PlayerUtils;
import org.bukkit.entity.Player;

public class ReloadModuleConfirmationGui extends ConfirmationGui {

    private final SupremePrisonModule module;

    public ReloadModuleConfirmationGui(Player player, SupremePrisonModule module) {
        super(player, module == null ? "Reload all modules ?" : "Reload module " + module.getName() + "?");
        this.module = module;
    }

    @Override
    public void confirm(boolean confirm) {
        if (confirm) {
            if (module == null) {
                SupremePrison.getInstance().getModules().forEach(module1 -> SupremePrison.getInstance().reloadModule(module1));
                PlayerUtils.sendMessage(this.getPlayer(), "&aSuccessfully reloaded all modules.");
            } else {
                SupremePrison.getInstance().reloadModule(module);
                PlayerUtils.sendMessage(this.getPlayer(), "&aSuccessfully reloaded &e&l" + this.module.getName() + " &amodule.");
            }
        }
        this.close();
    }
}
