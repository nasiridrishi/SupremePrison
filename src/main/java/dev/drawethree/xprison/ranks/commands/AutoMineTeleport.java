package dev.drawethree.xprison.ranks.commands;

import dev.drawethree.xprison.ranks.XPrisonRanks;
import me.lucko.helper.Commands;

public class AutoMineTeleport {

    private static final String[] COMMAND_ALIASES = {"automine", "autominetp", "mineauto"};
    private static final String PERMISSION_REQUIRED = "xprison.ranks.autominetp";
    private final XPrisonRanks plugin;

    public AutoMineTeleport(XPrisonRanks plugin) {
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
