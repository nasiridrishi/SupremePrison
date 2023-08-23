package net.primegames.supremeprison.gangs.commands.impl;

import net.primegames.supremeprison.gangs.commands.GangCommand;
import net.primegames.supremeprison.gangs.commands.GangSubCommand;
import net.primegames.supremeprison.gangs.enums.GangRenameResult;
import net.primegames.supremeprison.gangs.model.Gang;
import net.primegames.supremeprison.utils.player.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class GangRenameSubCommand extends GangSubCommand {

    public GangRenameSubCommand(GangCommand command) {
        super(command, "rename");
    }

    @Override
    public String getUsage() {
        return ChatColor.RED + "/gang rename [new_name]";
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (args.size() == 1 && sender instanceof Player) {
            Player p = (Player) sender;
            String newName = args.get(0);

            Optional<Gang> gangOptional = this.command.getPlugin().getGangsManager().getPlayerGang(p);

            if (!gangOptional.isPresent()) {
                PlayerUtils.sendMessage(p, this.command.getPlugin().getConfig().getMessage("not-in-gang"));
                return false;
            }

            Gang gang = gangOptional.get();

            if (!gang.isOwner(p)) {
                PlayerUtils.sendMessage(p, this.command.getPlugin().getConfig().getMessage("gang-not-owner"));
                return false;
            }

            return this.command.getPlugin().getGangsManager().renameGang(gang, newName, p) == GangRenameResult.SUCCESS;
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
