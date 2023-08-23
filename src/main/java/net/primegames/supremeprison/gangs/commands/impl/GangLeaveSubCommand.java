package net.primegames.supremeprison.gangs.commands.impl;

import net.primegames.supremeprison.gangs.commands.GangCommand;
import net.primegames.supremeprison.gangs.commands.GangSubCommand;
import net.primegames.supremeprison.gangs.enums.GangLeaveReason;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class GangLeaveSubCommand extends GangSubCommand {

    public GangLeaveSubCommand(GangCommand command) {
        super(command, "leave", "quit");
    }

    @Override
    public String getUsage() {
        return ChatColor.RED + "/gang leave";
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (args.size() == 0 && sender instanceof Player) {
            Player p = (Player) sender;
            return this.command.getPlugin().getGangsManager().leaveGang(p, GangLeaveReason.LEAVE);
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
