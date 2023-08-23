package net.primegames.supremeprison.mines.commands.impl;

import net.primegames.supremeprison.mines.SupremePrisonMines;
import net.primegames.supremeprison.mines.commands.MineCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MineCreateCommand extends MineCommand {

    public MineCreateCommand(SupremePrisonMines plugin) {
        super(plugin, "create", "new");
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (args.size() != 1) {
            return false;
        }
        if (!(sender instanceof Player)) {
            return false;
        }

        this.plugin.getManager().createMine((Player) sender, args.get(0));
        return true;
    }

    @Override
    public String getUsage() {
        return "&cUsage: /mines create <name> - Creates a new mine";
    }

    @Override
    public boolean canExecute(CommandSender sender) {
        return sender.hasPermission(SupremePrisonMines.MINES_ADMIN_PERM);
    }
}
