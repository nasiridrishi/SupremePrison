package net.primegames.supremeprison.prestiges.commands;

import net.primegames.supremeprison.prestiges.SupremePrisonPrestiges;
import me.lucko.helper.Commands;

public class PrestigeCommand {

    private final SupremePrisonPrestiges plugin;

    public PrestigeCommand(SupremePrisonPrestiges plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Commands.create()
                .assertPlayer()
                .handler(c -> {
                    if (c.args().size() == 0) {
                        this.plugin.getPrestigeManager().buyNextPrestige(c.sender());
                    }
                }).registerAndBind(this.plugin.getCore(), "prestige");
    }
}
