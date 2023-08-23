package net.primegames.supremeprison.ranks.commands;

import net.primegames.supremeprison.ranks.SupremePrisonRanks;
import me.lucko.helper.Commands;

public class MaxRankupCommand {

    private static final String[] COMMAND_ALIASES = {"maxrankup", "mru"};
    private static final String PERMISSION_REQUIRED = "supremeprison.ranks.maxrankup";
    private final SupremePrisonRanks plugin;

    public MaxRankupCommand(SupremePrisonRanks plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Commands.create()
                .assertPermission(PERMISSION_REQUIRED, this.plugin.getRanksConfig().getMessage("no_permission"))
                .assertPlayer()
                .handler(c -> {
                    if (c.args().isEmpty()) {
                        //this.plugin.getRanksManager().buyMaxRank(c.sender());
                    }
                }).registerAndBind(this.plugin.getCore(), COMMAND_ALIASES);
    }
}
