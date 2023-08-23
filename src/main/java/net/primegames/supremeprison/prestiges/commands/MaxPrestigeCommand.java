package net.primegames.supremeprison.prestiges.commands;

import net.primegames.supremeprison.prestiges.SupremePrisonPrestiges;
import me.lucko.helper.Commands;

public class MaxPrestigeCommand {

    private final SupremePrisonPrestiges plugin;

    public MaxPrestigeCommand(SupremePrisonPrestiges plugin) {

        this.plugin = plugin;
    }

    public void register() {
        Commands.create()
                .assertPermission("supremeprison.prestiges.maxprestige", this.plugin.getPrestigeConfig().getMessage("no_permission"))
                .assertPlayer()
                .handler(c -> {
                    if (c.args().size() == 0) {

                        if (this.plugin.getPrestigeManager().isPrestiging(c.sender())) {
                            return;
                        }

                        this.plugin.getPrestigeManager().buyMaxPrestige(c.sender());
                    }
                }).registerAndBind(this.plugin.getCore(), "maxprestige", "maxp", "mp");
    }
}
