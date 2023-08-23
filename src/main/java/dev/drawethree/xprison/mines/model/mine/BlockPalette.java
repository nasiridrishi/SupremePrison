package dev.drawethree.xprison.mines.model.mine;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import dev.drawethree.xprison.utils.compat.CompMaterial;
import me.lucko.helper.gson.GsonSerializable;
import me.lucko.helper.gson.JsonBuilder;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockPalette implements GsonSerializable {

    private final Map<CompMaterial, Double> blockPercentages;

    private RandomPattern randomPattern;

    public BlockPalette() {
        this.blockPercentages = new HashMap<>();
    }

    private BlockPalette(Map<CompMaterial, Double> blockPercentages) {
        this.blockPercentages = blockPercentages;
        refresHPattern();
    }

    public static BlockPalette deserialize(JsonElement element) {
        Preconditions.checkArgument(element.isJsonObject());
        JsonObject object = element.getAsJsonObject();

        Map<CompMaterial, Double> blocks = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            CompMaterial material = CompMaterial.valueOf(entry.getKey());
            double percentage = entry.getValue().getAsDouble();
            if (percentage <= 0.0) {
                continue;
            }
            blocks.put(material, percentage);
        }

        return new BlockPalette(blocks);
    }

    public boolean contains(CompMaterial material) {
        return blockPercentages.containsKey(material);
    }

    public double getPercentage(CompMaterial material) {
        return blockPercentages.getOrDefault(material, 0.0);
    }

    public void setPercentage(CompMaterial material, double newPercentage) {
        if (newPercentage <= 0.0) {
            this.blockPercentages.remove(material);
        } else {
            this.blockPercentages.put(material, newPercentage);
        }
        refresHPattern();
    }

    public void addToPalette(CompMaterial material, double percentage) {
        this.blockPercentages.put(material, percentage);
        refresHPattern();
    }

    public void removeFromPalette(CompMaterial material) {
        this.blockPercentages.remove(material);
        refresHPattern();
    }

    public Set<CompMaterial> getMaterials() {
        return this.blockPercentages.keySet();
    }

    public Set<CompMaterial> getValidMaterials() {
        return this.blockPercentages.keySet().stream().filter(material -> getPercentage(material) > 0.0).collect(Collectors.toSet());
    }

    public double getTotalPercentage() {
        return this.blockPercentages.values().stream().mapToDouble(Double::valueOf).sum();
    }

    @Nonnull
    @Override
    public JsonElement serialize() {
        JsonBuilder.JsonObjectBuilder builder = JsonBuilder.object();

        for (Map.Entry<CompMaterial, Double> entry : blockPercentages.entrySet()) {
            builder.addIfAbsent(entry.getKey().name(), entry.getValue());
        }
        return builder.build();
    }

    public boolean isEmpty() {
        return this.blockPercentages.isEmpty() || this.getTotalPercentage() <= 0.0;
    }

    private void refresHPattern() {
        randomPattern = new RandomPattern();
        getValidMaterials().forEach(compMaterial -> {
            //randomPattern.add(BukkitAdapter.adapt(cataMineBlock.getBlockData()).toBaseBlock(), cataMineBlock.getChance()))
            randomPattern.add(BukkitAdapter.adapt(compMaterial.toMaterial().createBlockData()).toBaseBlock(), getPercentage(compMaterial));
        });
    }

    public Pattern getRandomPattern() {
        if (randomPattern == null) {
            refresHPattern();
        }
        return randomPattern;
    }
}
