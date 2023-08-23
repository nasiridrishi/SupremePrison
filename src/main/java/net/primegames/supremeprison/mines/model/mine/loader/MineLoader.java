package net.primegames.supremeprison.mines.model.mine.loader;

import net.primegames.supremeprison.mines.model.mine.Mine;

public interface MineLoader<T> {

    Mine load(T type);
}
