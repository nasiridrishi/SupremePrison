package net.primegames.supremeprison.mines.commands.impl;

import net.primegames.supremeprison.mines.SupremePrisonMines;
import net.primegames.supremeprison.mines.commands.MineCommand;
import net.primegames.supremeprison.mines.model.mine.Mine;
import net.primegames.supremeprison.utils.player.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MineSetTpCommand extends MineCommand {

    public MineSetTpCommand(SupremePrisonMines plugin) {
        super(plugin, "settp", "tpset");
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        if (args.size() != 1) {
            return false;
        }

        Mine mine = this.plugin.getManager().getMineByName(args.get(0));

        if (mine == null) {
            PlayerUtils.sendMessage(sender, this.plugin.getMessage("mine_not_exists").replace("%mine%", args.get(0)));
            return true;
        }

        return this.plugin.getManager().setTeleportLocation((Player) sender, mine);
    }

    @Override
    public String getUsage() {
        return "&cUsage: /mines settp <mine> - Sets the teleport location of specified mine";
    }

    @Override
    public boolean canExecute(CommandSender sender) {
        return sender.hasPermission(SupremePrisonMines.MINES_ADMIN_PERM);
    }
}
