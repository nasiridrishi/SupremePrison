package net.primegames.supremeprison.mines.commands.impl;

import net.primegames.supremeprison.mines.SupremePrisonMines;
import net.primegames.supremeprison.mines.model.mine.Mine;
import net.primegames.supremeprison.utils.text.TextUtils;
import me.lucko.helper.Commands;
import org.bukkit.Bukkit;

public class ResetMineCommand {

    private static final String[] COMMAND_ALIASES = {"resetmine", "minereset"};

    private static final String PERMISSION_REQUIRED = "supremeprison.command.resetmine";


    private final SupremePrisonMines plugin;

    public ResetMineCommand(SupremePrisonMines plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Commands.create()
                //send message in colors
                .assertPermission(PERMISSION_REQUIRED, "&cYou do not have permission to use this command")
                .assertPlayer()
                .handler(c -> {
                    if (c.args().isEmpty()) {
                        if (c.sender().hasPermission("supremeprison.command.resetmine.cooldown") && !c.sender().hasPermission("supremeprison.command.resetmine.cooldown.bypass")) {
                            TextUtils.sendMessage(c.sender(), this.plugin.getMessage("mine_reset__command_cooldown"));
                        } else {
                            Mine mine = this.plugin.getManager().getMineAtLocation(c.sender().getLocation());
                            if (mine == null) {
                                TextUtils.sendMessage(c.sender(), this.plugin.getMessage("must_be_standing_in_mine"));
                                return;
                            }
                            if (mine.isResetting()) {
                                TextUtils.sendMessage(c.sender(), this.plugin.getMessage("mine_already_reset").replace("%mine%", mine.getName()));
                                return;
                            }
                            this.plugin.getManager().resetMine(mine);
                            c.sender().sendMessage(plugin.getMessage("mine_reset_started").replace("%mine%", mine.getName()));
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + c.sender().getName() + "permission settemp supremeprison.command.resetmine.cooldown true 5min");
                        }
                    }
                }).registerAndBind(this.plugin.getCore(), COMMAND_ALIASES);
    }


}
