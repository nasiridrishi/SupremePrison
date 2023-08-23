package dev.drawethree.xprison.mines.model.mine.reset;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import dev.drawethree.xprison.XPrison;
import dev.drawethree.xprison.mines.XPrisonMines;
import dev.drawethree.xprison.mines.model.mine.BlockPalette;
import dev.drawethree.xprison.mines.model.mine.Mine;
import org.bukkit.Bukkit;

import java.util.function.Consumer;


public class InstantReset extends ResetType {

    InstantReset() {
        super("Instant");
    }

    @Override
    public void reset(Mine mine, BlockPalette blockPalette, Consumer<Mine> onCompletion) {
        if (blockPalette.isEmpty()) {
            XPrison.getInstance().getLogger().warning("Reset for Mine " + mine.getName() + " aborted. Block palette is empty.");
            return;
        }
        XPrison.getInstance().getLogger().info("MineReset: Queuing Mine " + mine.getName() + " for reset");
        XPrisonMines.getInstance().getManager().getInstantAyncResetQueue().enqueueTask(() -> {
            try (EditSession editSession = WorldEdit.getInstance().newEditSession(mine.getMineRegion().getWorld())) {
                editSession.setBlocks(mine.getMineRegion(), blockPalette.getRandomPattern());
            } catch (MaxChangedBlocksException e) {
                Bukkit.getLogger().severe("MaxChangedBlocksException while resetting Mine " + mine.getName());
                e.printStackTrace();
            } finally {
                Bukkit.getScheduler().runTask(XPrison.getInstance(), () -> onCompletion.accept(mine));
            }
        });
    }
}
