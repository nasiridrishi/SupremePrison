package net.primegames.supremeprison.mines.commands;

import net.primegames.supremeprison.mines.SupremePrisonMines;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.List;

@Getter
public abstract class MineCommand {

    private final String name;
    @Getter
    private final String[] aliases;
    protected SupremePrisonMines plugin;

    public MineCommand(SupremePrisonMines plugin, String name, String... aliases) {
        this.plugin = plugin;
        this.name = name;
        this.aliases = aliases;
    }

    public abstract boolean execute(CommandSender sender, List<String> args);

    public abstract String getUsage();

    public abstract boolean canExecute(CommandSender sender);
}
