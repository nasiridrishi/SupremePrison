package net.primegames.supremeprison.mines.model.mine.reset;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.mines.SupremePrisonMines;
import net.primegames.supremeprison.mines.model.mine.BlockPalette;
import net.primegames.supremeprison.mines.model.mine.Mine;
import org.bukkit.Bukkit;

import java.util.function.Consumer;


public class InstantReset extends ResetType {

    InstantReset() {
        super("Instant");
    }

    @Override
    public void reset(Mine mine, BlockPalette blockPalette, Consumer<Mine> onCompletion) {
        if (blockPalette.isEmpty()) {
            SupremePrison.getInstance().getLogger().warning("Reset for Mine " + mine.getName() + " aborted. Block palette is empty.");
            return;
        }
        SupremePrison.getInstance().getLogger().info("MineReset: Queuing Mine " + mine.getName() + " for reset");
        SupremePrisonMines.getInstance().getManager().getInstantAyncResetQueue().enqueueTask(() -> {
            try (EditSession editSession = WorldEdit.getInstance().newEditSession(mine.getMineRegion().getWorld())) {
                editSession.setBlocks(mine.getMineRegion(), blockPalette.getRandomPattern());
            } catch (MaxChangedBlocksException e) {
                Bukkit.getLogger().severe("MaxChangedBlocksException while resetting Mine " + mine.getName());
                e.printStackTrace();
            } finally {
                Bukkit.getScheduler().runTask(SupremePrison.getInstance(), () -> onCompletion.accept(mine));
            }
        });
    }
}
