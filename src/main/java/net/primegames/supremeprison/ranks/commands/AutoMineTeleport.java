package net.primegames.supremeprison.ranks.commands;

import net.primegames.supremeprison.ranks.SupremePrisonRanks;
import me.lucko.helper.Commands;

public class AutoMineTeleport {

    private static final String[] COMMAND_ALIASES = {"automine", "autominetp", "mineauto"};
    private static final String PERMISSION_REQUIRED = "supremeprison.ranks.autominetp";
    private final SupremePrisonRanks plugin;

    public AutoMineTeleport(SupremePrisonRanks plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Commands.create()
                .assertPermission(PERMISSION_REQUIRED, this.plugin.getRanksConfig().getMessage("no_permission"))
                .assertPlayer()
                .handler(c -> {
                    if (c.args().isEmpty()) {
                        this.plugin.getRanksManager().attemptRankMineTeleport(this.plugin.getRanksManager().getPlayerRank(c.sender()), c.sender(), 3);
                    }
                }).registerAndBind(this.plugin.getCore(), COMMAND_ALIASES);
    }


}
