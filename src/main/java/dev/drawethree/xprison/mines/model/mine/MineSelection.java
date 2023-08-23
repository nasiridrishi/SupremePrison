package dev.drawethree.xprison.mines.model.mine;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lucko.helper.serialize.Position;
import me.lucko.helper.serialize.Region;
import org.bukkit.Bukkit;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MineSelection {
    private Position pos1;
    private Position pos2;

    public static Region fromWeRegion(com.sk89q.worldedit.regions.Region region) {
        return Region.of(Position.of(region.getMinimumPoint().toVector3().getX(), region.getMinimumPoint().toVector3().getY(), region.getMinimumPoint().toVector3().getZ(), Objects.requireNonNull(Bukkit.getWorld(region.getWorld().getName()))),
                Position.of(region.getMaximumPoint().toVector3().getX(), region.getMaximumPoint().toVector3().getY(), region.getMaximumPoint().toVector3().getZ(), Objects.requireNonNull(Bukkit.getWorld(region.getWorld().getName()))));
    }

    public static com.sk89q.worldedit.regions.Region toWeCuboidRegion(Region region) {
        return new com.sk89q.worldedit.regions.CuboidRegion(BukkitAdapter.adapt(region.getMax().toLocation().getWorld()), BukkitAdapter.asBlockVector(region.getMin().toLocation()), BukkitAdapter.asBlockVector(region.getMax().toLocation()));
    }

    public boolean isValid() {
        return pos1 != null && pos2 != null;
    }

    public Region toRegion() {
        return pos1.regionWith(pos2);
    }

    public com.sk89q.worldedit.regions.Region toWeCuboidRegion() {
        return MineSelection.toWeCuboidRegion(toRegion());
    }
}
