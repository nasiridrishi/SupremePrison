package net.primegames.supremeprison.gangs.commands.impl;

import net.primegames.supremeprison.gangs.commands.GangCommand;
import net.primegames.supremeprison.gangs.commands.GangSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public final class GangTopSubCommand extends GangSubCommand {

    public GangTopSubCommand(GangCommand command) {
        super(command, "top", "leaderboard");
    }

    @Override
    public String getUsage() {
        return ChatColor.RED + "/gang top";
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (args.size() == 0) {
            return this.command.getPlugin().getGangsManager().sendGangTop(sender);
        }
        return false;
    }


    @Override
    public boolean canExecute(CommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return new ArrayList<>();
    }
}
