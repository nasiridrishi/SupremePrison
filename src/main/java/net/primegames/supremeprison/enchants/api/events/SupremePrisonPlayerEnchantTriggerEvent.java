package net.primegames.supremeprison.enchants.api.events;

import net.primegames.supremeprison.api.events.player.SupremePrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.util.List;

@Getter
public abstract class SupremePrisonPlayerEnchantTriggerEvent extends SupremePrisonPlayerEvent implements Cancellable {

    protected final Player player;
    protected final IWrappedRegion mineRegion;
    protected final Block originBlock;
    protected final List<Block> blocksAffected;

    public SupremePrisonPlayerEnchantTriggerEvent(Player p, IWrappedRegion mineRegion, Block originBlock, List<Block> blocksAffected) {
        super(p);
        this.player = p;
        this.mineRegion = mineRegion;
        this.originBlock = originBlock;
        this.blocksAffected = blocksAffected;
    }
}
