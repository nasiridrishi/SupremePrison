package dev.drawethree.xprison.mines.model.mine.reset;

import com.sk89q.worldedit.MaxChangedBlocksException;
import dev.drawethree.xprison.mines.model.mine.BlockPalette;
import dev.drawethree.xprison.mines.model.mine.Mine;
import lombok.Getter;

import java.util.function.Consumer;

@Getter
public abstract class ResetType {
    public static final InstantReset INSTANT = new InstantReset();

    private final String name;

    ResetType(String paramString) {
        this.name = paramString;
    }

    public abstract void reset(Mine paramMine, BlockPalette blockPalette, Consumer<Mine> onCompletion) throws MaxChangedBlocksException;


}