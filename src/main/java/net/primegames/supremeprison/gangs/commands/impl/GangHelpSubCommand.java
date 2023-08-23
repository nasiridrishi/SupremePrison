package net.primegames.supremeprison.gangs.commands.impl;

import net.primegames.supremeprison.gangs.commands.GangCommand;
import net.primegames.supremeprison.gangs.commands.GangSubCommand;
import net.primegames.supremeprison.gangs.utils.GangsConstants;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public final class GangHelpSubCommand extends GangSubCommand {

    public GangHelpSubCommand(GangCommand command) {
        super(command, "help", "?");
    }

    @Override
    public String getUsage() {
        return ChatColor.RED + "/gang help";
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (args.isEmpty()) {
            this.command.getPlugin().getGangsManager().sendHelpMenu(sender);
            if (sender.hasPermission(GangsConstants.GANGS_ADMIN_PERM)) {
                this.command.getPlugin().getGangsManager().sendAdminHelpMenu(sender);
            }
            return true;
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
