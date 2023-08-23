package net.primegames.supremeprison.mines.migration.model.impl;

import com.koletar.jj.mineresetlite.Mine;
import com.koletar.jj.mineresetlite.MineResetLite;
import com.koletar.jj.mineresetlite.SerializableBlock;
import net.primegames.supremeprison.mines.migration.model.MinesMigration;
import net.primegames.supremeprison.mines.model.mine.BlockPalette;
import net.primegames.supremeprison.mines.model.mine.MineSelection;
import net.primegames.supremeprison.utils.compat.CompMaterial;
import me.lucko.helper.serialize.Point;
import me.lucko.helper.serialize.Position;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map;

public final class MineResetLiteMigration extends MinesMigration<Mine> {

    private final MineResetLite plugin;

    public MineResetLiteMigration() {
        super("MineResetLite");
        this.plugin = ((MineResetLite) Bukkit.getPluginManager().getPlugin(this.fromPlugin));
    }

    @Override
    protected List<Mine> getMinesToMigrate() {
        return this.plugin.mines;
    }

    @Override
    protected boolean preValidateMigration() {
        return this.plugin != null;
    }

    @Override
    protected net.primegames.supremeprison.mines.model.mine.Mine migrate(Mine mine) {
        net.primegames.supremeprison.mines.model.mine.Mine.Builder builder = new net.primegames.supremeprison.mines.model.mine.Mine.Builder();

        //Name
        String name = mine.getName();
        builder.name(name);

        //Region
        Position minPoint = Position.of(mine.getMinX(), mine.getMinY(), mine.getMinZ(), mine.getWorld());
        Position maxPoint = Position.of(mine.getMaxX(), mine.getMaxY(), mine.getMaxZ(), mine.getWorld());
        builder.region(MineSelection.toWeCuboidRegion(minPoint.regionWith(maxPoint)));

        //Teleport location
        if (mine.getTpPos() != null) {
            Point teleportLocation = Point.of(mine.getTpPos());
            builder.teleportLocation(teleportLocation);
        }

        //Block palette
        BlockPalette blockPalette = new BlockPalette();

        for (Map.Entry<SerializableBlock, Double> entry : mine.getComposition().entrySet()) {
            double chance = entry.getValue();
            CompMaterial material = CompMaterial.fromId(entry.getKey().getBlockId(), entry.getKey().getData());
            blockPalette.addToPalette(material, chance);
        }

        builder.blockPalette(blockPalette);
        return builder.build();
    }
}
