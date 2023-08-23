package net.primegames.supremeprison.gangs.commands.impl;

import net.primegames.supremeprison.gangs.commands.GangCommand;
import net.primegames.supremeprison.gangs.commands.GangSubCommand;
import net.primegames.supremeprison.gangs.enums.GangCreateResult;
import net.primegames.supremeprison.gangs.utils.GangsConstants;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class GangCreateSubCommand extends GangSubCommand {

    public GangCreateSubCommand(GangCommand command) {
        super(command, "create", "new");
    }

    @Override
    public String getUsage() {
        return ChatColor.RED + "/gang create <name>";
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (sender instanceof Player && args.size() == 1) {
            return this.command.getPlugin().getGangsManager().createGang(args.get(0), (Player) sender) == GangCreateResult.SUCCESS;
        }
        return false;
    }

    @Override
    public boolean canExecute(CommandSender sender) {
        return sender.hasPermission(GangsConstants.GANGS_CREATE_PERM);
    }

    @Override
    public List<String> getTabComplete() {
        return new ArrayList<>();
    }
}
