package dev.drawethree.xprison.mines.commands;

import dev.drawethree.xprison.mines.XPrisonMines;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.List;

@Getter
public abstract class MineCommand {

    private final String name;
    @Getter
    private final String[] aliases;
    protected XPrisonMines plugin;

    public MineCommand(XPrisonMines plugin, String name, String... aliases) {
        this.plugin = plugin;
        this.name = name;
        this.aliases = aliases;
    }

    public abstract boolean execute(CommandSender sender, List<String> args);

    public abstract String getUsage();

    public abstract boolean canExecute(CommandSender sender);
}
