package net.primegames.supremeprison.tokens.config;

import net.primegames.supremeprison.config.FileManager;
import net.primegames.supremeprison.tokens.SupremePrisonTokens;
import net.primegames.supremeprison.tokens.model.BlockReward;
import net.primegames.supremeprison.utils.text.TextUtils;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BlockRewardsConfig {

    private final SupremePrisonTokens plugin;
    private final FileManager.Config config;
    @Getter
    private final Map<Long, BlockReward> blockRewards;

    public BlockRewardsConfig(SupremePrisonTokens plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getCore().getFileManager().getConfig("block-rewards.yml").copyDefaults(true).save();
        this.blockRewards = new LinkedHashMap<>();
    }

    private FileManager.Config getConfig() {
        return this.config;
    }

    public YamlConfiguration getYamlConfig() {
        return this.config.get();
    }

    public void load() {
        YamlConfiguration configuration = getYamlConfig();
        this.loadVariables(configuration);
    }

    public void reload() {
        this.getConfig().reload();
        this.load();
    }

    private void loadVariables(YamlConfiguration configuration) {
        this.blockRewards.clear();
        ConfigurationSection section = configuration.getConfigurationSection("block-rewards");

        if (section != null) {
            for (String key : section.getKeys(false)) {
                long blocksNeeded = Long.parseLong(key);
                String message = TextUtils.applyColor(configuration.getString("block-rewards." + key + ".message"));
                List<String> commands = configuration.getStringList("block-rewards." + key + ".commands");
                BlockReward reward = new BlockReward(blocksNeeded, commands, message);
                this.blockRewards.put(blocksNeeded, reward);
            }
        }
        this.plugin.getCore().getLogger().info("Loaded " + this.blockRewards.keySet().size() + " Block Rewards!");
    }

}
