package net.primegames.supremeprison.autosell.command;

import net.primegames.supremeprison.autosell.SupremePrisonAutoSell;
import me.lucko.helper.Commands;

public class SellAllCommand {

    private static final String COMMAND_NAME = "sellall";
    private final SupremePrisonAutoSell plugin;

    public SellAllCommand(SupremePrisonAutoSell plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Commands.create()
                .assertPlayer()
                .handler(c -> {

                    String regionName = null;

                    if (c.args().size() == 1) {
                        regionName = c.rawArg(0);
                    }

                    this.plugin.getManager().sellAll(c.sender(), regionName);

                }).registerAndBind(this.plugin.getCore(), COMMAND_NAME);
    }
}
