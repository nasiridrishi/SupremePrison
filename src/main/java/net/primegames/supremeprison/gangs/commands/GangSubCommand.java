package net.primegames.supremeprison.gangs.commands;

import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class GangSubCommand {

    protected final GangCommand command;
    protected final Map<String, GangSubCommand> subCommands;
    @Getter
    private final String[] aliases;

    public GangSubCommand(GangCommand command, String... aliases) {
        this.command = command;
        this.aliases = aliases;
        this.subCommands = new HashMap<>();
    }

    public abstract boolean execute(CommandSender sender, List<String> args);

    public abstract String getUsage();

    public abstract boolean canExecute(CommandSender sender);

    public abstract List<String> getTabComplete();

    protected void registerSubCommand(GangSubCommand subCommand) {
        for (String alias : subCommand.getAliases()) {
            this.subCommands.put(alias.toLowerCase(), subCommand);
        }
    }

    protected GangSubCommand getSubCommand(String name) {
        return subCommands.get(name.toLowerCase());
    }


}
