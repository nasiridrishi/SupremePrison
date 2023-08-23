package net.primegames.supremeprison.autominer.command;

import net.primegames.supremeprison.autominer.SupremePrisonAutoMiner;
import net.primegames.supremeprison.autominer.utils.AutoMinerUtils;
import net.primegames.supremeprison.utils.player.PlayerUtils;
import me.lucko.helper.Commands;
import me.lucko.helper.command.context.CommandContext;
import org.bukkit.entity.Player;

public class AutoMinerCommand {

    private static final String[] COMMAND_ALIASES = {"autominer"};

    private final SupremePrisonAutoMiner plugin;

    public AutoMinerCommand(SupremePrisonAutoMiner plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Commands.create()
                .assertPlayer()
                .handler(c -> {

                    if (!validateArguments(c)) {
                        return;
                    }

                    int timeLeft = this.plugin.getManager().getAutoMinerTime(c.sender());
                    PlayerUtils.sendMessage(c.sender(), this.plugin.getAutoMinerConfig().getMessage("auto_miner_time").replace("%time%", AutoMinerUtils.getAutoMinerTimeLeftFormatted(timeLeft)));

                }).registerAndBind(this.plugin.getCore(), COMMAND_ALIASES);
    }

    private boolean validateArguments(CommandContext<Player> c) {
        return c.args().size() == 0;
    }
}
