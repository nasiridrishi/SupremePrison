package net.primegames.supremeprison.ranks.commands;

import net.primegames.supremeprison.ranks.SupremePrisonRanks;
import net.primegames.supremeprison.ranks.model.Rank;
import net.primegames.supremeprison.utils.player.PlayerUtils;
import me.lucko.helper.Commands;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SetRankCommand {

    private static final String PERMISSION_REQUIRED = "supremeprison.ranks.admin";
    private static final String[] COMMAND_ALIASES = {"setrank"};

    private final SupremePrisonRanks plugin;

    public SetRankCommand(SupremePrisonRanks plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Commands.create()
                .assertPermission(PERMISSION_REQUIRED)
                .handler(c -> {
                    if (c.args().size() == 2) {
                        Player target = c.arg(0).parseOrFail(Player.class);
                        Optional<Rank> rankOptional = this.plugin.getRanksManager().getRankById(c.arg(1).parseOrFail(Integer.class));

                        if (!rankOptional.isPresent()) {
                            PlayerUtils.sendMessage(c.sender(), "&cInvalid rank id provided.");
                            return;
                        }

                        this.plugin.getRanksManager().setRank(target, rankOptional.get(), c.sender());
                    }
                }).registerAndBind(this.plugin.getCore(), COMMAND_ALIASES);
    }
}
