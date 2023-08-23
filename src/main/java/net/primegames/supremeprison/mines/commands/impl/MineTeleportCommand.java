package net.primegames.supremeprison.mines.commands.impl;

import net.primegames.supremeprison.mines.SupremePrisonMines;
import net.primegames.supremeprison.mines.commands.MineCommand;
import net.primegames.supremeprison.mines.model.mine.Mine;
import net.primegames.supremeprison.utils.player.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MineTeleportCommand extends MineCommand {

    public MineTeleportCommand(SupremePrisonMines plugin) {
        super(plugin, "minetp");
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        if (args.isEmpty()) {
            return false;
        }

        Mine mine = this.plugin.getManager().getMineByName(args.get(0));

        if (mine == null) {
            PlayerUtils.sendMessage(sender, this.plugin.getMessage("mine_not_exists").replace("%mine%", args.get(0)));
            return true;
        }

        if (args.size() > 1) {
            Player player = Bukkit.getPlayer(args.get(1));
            if (player == null) {
                PlayerUtils.sendMessage(sender, this.plugin.getMessage("player_not_exists").replace("%player%", args.get(1)));
                return true;
            }

            boolean force = args.size() > 2 && args.get(2).equalsIgnoreCase("-f");

            if (!mine.canTeleport(player) && !force) {
                PlayerUtils.sendMessage(sender, this.plugin.getMessage("no_permission"));
                return true;
            }

            this.plugin.getManager().teleportToMine(player, mine);
            return true;
        }

        if (!mine.canTeleport((Player) sender)) {
            PlayerUtils.sendMessage(sender, this.plugin.getMessage("no_permission"));
            return true;
        }

        this.plugin.getManager().teleportToMine((Player) sender, mine);
        return true;
    }

    @Override
    public String getUsage() {
        return "&cUsage: /minetp teleport <mine> <player[optional]> -f[optional] - Teleports you or others to a specified mine";
    }

    @Override
    public boolean canExecute(CommandSender sender) {
        return true;
    }
}
