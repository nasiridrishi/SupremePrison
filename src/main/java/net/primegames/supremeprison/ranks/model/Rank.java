package net.primegames.supremeprison.ranks.model;

import net.primegames.supremeprison.mines.model.mine.Mine;
import lombok.Getter;

import java.util.List;

@Getter
public class Rank {

    private final int id;
    private final double cost;
    private final String prefix;
    private final List<String> commandsToExecute;
    private final Mine mine;
    private final String name;


    public Rank(int id, double cost, String name, String prefix, List<String> commandsToExecute) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.prefix = prefix;
        this.commandsToExecute = commandsToExecute;
        this.mine = null;
    }


    public Rank(int id, double cost, String name, String prefix, List<String> commandsToExecute, Mine mine) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.prefix = prefix;
        this.commandsToExecute = commandsToExecute;
        this.mine = mine;
    }


}
