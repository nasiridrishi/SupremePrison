package net.primegames.supremeprison.prestiges.config;

import net.primegames.supremeprison.config.FileManager;
import net.primegames.supremeprison.prestiges.SupremePrisonPrestiges;
import net.primegames.supremeprison.prestiges.model.Prestige;
import net.primegames.supremeprison.utils.text.TextUtils;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PrestigeConfig {

    private final SupremePrisonPrestiges plugin;
    private final FileManager.Config config;
    @Getter
    private final Map<Long, Prestige> prestigeById;
    @Getter
    private Prestige maxPrestige;
    @Getter
    private String unlimitedPrestigePrefix;
    @Getter
    private List<String> prestigeTopFormat;
    @Getter
    private List<String> unlimitedPrestigesRewardPerPrestige;
    @Getter
    private Map<String, String> messages;
    @Getter
    private Map<Long, List<String>> unlimitedPrestigesRewards;
    @Getter
    private int topPlayersAmount;
    @Getter
    private long unlimitedPrestigeMax;
    @Getter
    private double unlimitedPrestigeCost;
    @Getter
    private double increaseCostBy;
    @Getter
    private boolean useTokensCurrency;
    @Getter
    private boolean unlimitedPrestiges;
    @Getter
    private boolean increaseCostEnabled;
    @Getter
    private boolean resetRankAfterPrestige;
    private int savePlayerDataInterval;

    @Getter
    private String rankUpMultiplierFormula;

    public PrestigeConfig(SupremePrisonPrestiges plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getCore().getFileManager().getConfig("prestiges.yml").copyDefaults(true).save();
        this.prestigeById = new HashMap<>();
    }


    private void loadMessages(YamlConfiguration configuration) {
        this.messages = new HashMap<>();

        for (String key : configuration.getConfigurationSection("messages").getKeys(false)) {
            messages.put(key.toLowerCase(), TextUtils.applyColor(getConfig().get().getString("messages." + key)));
        }
    }

    public void reload() {
        YamlConfiguration configuration = getYamlConfig();
        this.loadVariables(configuration);
        this.loadPrestiges(configuration);
        this.loadUnlimitedPrestigesRewards(configuration);
        this.loadMessages(configuration);
    }

    private void loadPrestiges(YamlConfiguration configuration) {
        this.prestigeById.clear();

        if (this.unlimitedPrestiges) {
            this.plugin.getCore().getLogger().info(String.format("Loaded %,d prestiges.", this.unlimitedPrestigeMax));
        } else {
            for (String key : configuration.getConfigurationSection("Prestige").getKeys(false)) {
                long id = Long.parseLong(key);
                String prefix = TextUtils.applyColor(configuration.getString("Prestige." + key + ".Prefix"));
                long cost = configuration.getLong("Prestige." + key + ".Cost");
                List<String> commands = configuration.getStringList("Prestige." + key + ".CMD");
                Prestige p = new Prestige(id, cost, prefix, commands);
                this.prestigeById.put(id, p);
                this.maxPrestige = p;
            }
            this.plugin.getCore().getLogger().info(String.format("Loaded %,d prestiges!", this.prestigeById.keySet().size()));
        }
    }

    public void load() {
        this.reload();
    }


    public String getMessage(String messageKey) {
        return this.messages.getOrDefault(messageKey.toLowerCase(), "Missing message with key: " + messageKey);
    }

    private void loadVariables(YamlConfiguration configuration) {
        this.prestigeTopFormat = configuration.getStringList("prestige-top-format");
        this.unlimitedPrestiges = configuration.getBoolean("unlimited_prestiges.enabled");
        this.unlimitedPrestigeCost = configuration.getDouble("unlimited_prestiges.prestige_cost");
        this.unlimitedPrestigePrefix = TextUtils.applyColor(configuration.getString("unlimited_prestiges.prefix"));
        this.unlimitedPrestigeMax = configuration.getLong("unlimited_prestiges.max_prestige");
        this.increaseCostEnabled = configuration.getBoolean("unlimited_prestiges.increase_cost.enabled");
        this.increaseCostBy = configuration.getDouble("unlimited_prestiges.increase_cost.increase_cost_by");
        boolean unlimitedPrestigesRewardPerPrestigeEnabled = configuration.getBoolean("unlimited_prestiges.rewards-per-prestige.enabled");
        if (unlimitedPrestigesRewardPerPrestigeEnabled) {
            this.unlimitedPrestigesRewardPerPrestige = configuration.getStringList("unlimited_prestiges.rewards-per-prestige.rewards");
        }
        this.topPlayersAmount = configuration.getInt("top_players_amount");
        this.savePlayerDataInterval = configuration.getInt("player_data_save_interval");
        this.resetRankAfterPrestige = configuration.getBoolean("reset_rank_after_prestige");
        this.useTokensCurrency = configuration.getBoolean("use_tokens_currency");
        this.plugin.getCore().getLogger().info("Using " + (useTokensCurrency ? "Tokens" : "Money") + " currency for Prestiges.");
        this.rankUpMultiplierFormula = configuration.getString("rank_up_multiplier_formula");
    }

    private void loadUnlimitedPrestigesRewards(YamlConfiguration configuration) {
        this.unlimitedPrestigesRewards = new LinkedHashMap<>();

        ConfigurationSection section = configuration.getConfigurationSection("unlimited_prestiges.rewards");

        if (section == null) {
            return;
        }

        for (String key : section.getKeys(false)) {
            try {
                long id = Long.parseLong(key);

                List<String> rewards = section.getStringList(key);

                if (!rewards.isEmpty()) {
                    this.unlimitedPrestigesRewards.put(id, rewards);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private FileManager.Config getConfig() {
        return this.config;
    }

    public YamlConfiguration getYamlConfig() {
        return this.config.get();
    }

    public long getSavePlayerDataInterval() {
        return savePlayerDataInterval;
    }

}
