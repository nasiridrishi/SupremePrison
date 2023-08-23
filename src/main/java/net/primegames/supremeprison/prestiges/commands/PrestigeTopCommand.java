package net.primegames.supremeprison.prestiges.commands;

import net.primegames.supremeprison.prestiges.SupremePrisonPrestiges;
import me.lucko.helper.Commands;

public class PrestigeTopCommand {

    private final SupremePrisonPrestiges plugin;

    public PrestigeTopCommand(SupremePrisonPrestiges plugin) {

        this.plugin = plugin;
    }

    public void register() {
        Commands.create()
                .handler(c -> {
                    if (c.args().size() == 0) {
                        this.plugin.getPrestigeManager().sendPrestigeTop(c.sender());
                    }
                }).registerAndBind(this.plugin.getCore(), "prestigetop");
    }
}
