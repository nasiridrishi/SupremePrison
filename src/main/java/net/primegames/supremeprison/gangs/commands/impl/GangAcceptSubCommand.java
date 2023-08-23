package net.primegames.supremeprison.gangs.commands.impl;

import net.primegames.supremeprison.gangs.commands.GangCommand;
import net.primegames.supremeprison.gangs.commands.GangSubCommand;
import net.primegames.supremeprison.gangs.model.Gang;
import net.primegames.supremeprison.utils.player.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class GangAcceptSubCommand extends GangSubCommand {

    public GangAcceptSubCommand(GangCommand command) {
        super(command, "accept", "join");
    }

    @Override
    public String getUsage() {
        return ChatColor.RED + "/gang accept <gang>";
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (sender instanceof Player && args.size() == 1) {

            String gangName = args.get(0);
            Optional<Gang> gangOptional = this.command.getPlugin().getGangsManager().getGangWithName(gangName);

            if (!gangOptional.isPresent()) {
                PlayerUtils.sendMessage(sender, this.command.getPlugin().getConfig().getMessage("gang-not-exists"));
                return false;
            }

            return this.command.getPlugin().getGangsManager().acceptInvite((Player) sender, gangOptional.get());
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
