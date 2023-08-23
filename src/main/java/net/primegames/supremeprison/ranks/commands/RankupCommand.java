package net.primegames.supremeprison.ranks.commands;

import net.primegames.supremeprison.ranks.SupremePrisonRanks;
import me.lucko.helper.Commands;

public class RankupCommand {

    private static final String[] COMMAND_ALIASES = {"rankup"};
    private final SupremePrisonRanks plugin;

    public RankupCommand(SupremePrisonRanks plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Commands.create()
                .assertPlayer()
                .handler(c -> {
                    if (c.args().size() == 0) {
                        this.plugin.getRanksManager().buyNextRank(c.sender());
                    }
                }).registerAndBind(this.plugin.getCore(), COMMAND_ALIASES);
    }
}
