package net.primegames.supremeprison.tokens.config;

import net.primegames.supremeprison.config.FileManager;
import net.primegames.supremeprison.tokens.SupremePrisonTokens;
import net.primegames.supremeprison.utils.compat.CompMaterial;
import net.primegames.supremeprison.utils.text.TextUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokensConfig {

    private final FileManager.Config config;
    private final SupremePrisonTokens plugin;

    @Getter
    private double chance;
    @Getter
    private long minAmount;
    @Getter
    private long maxAmount;
    @Getter
    private long commandCooldown;
    @Getter
    private long nextResetWeekly;
    @Getter
    private long startingTokens;
    @Getter
    private int savePlayerDataInterval;
    @Getter
    private boolean displayTokenMessages;
    private Map<String, String> messages;
    private Map<Material, List<String>> luckyBlockRewards;
    @Getter
    private List<String> tokensTopFormat;
    @Getter
    private List<String> blocksTopFormat;
    @Getter
    private List<String> blocksTopFormatWeekly;
    @Getter
    private List<String> tokenItemLore;
    @Getter
    private String tokenItemDisplayName;
    @Getter
    private ItemStack tokenItem;
    @Getter
    private int topPlayersAmount;

    @Getter
    private String[] tokensCommandAliases;
    @Getter
    private String[] tokensTopCommandAliases;
    @Getter
    private String[] blocksTopCommandAliases;


    public TokensConfig(SupremePrisonTokens plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getCore().getFileManager().getConfig("tokens.yml").copyDefaults(true).save();
    }

    private FileManager.Config getConfig() {
        return this.config;
    }

    public YamlConfiguration getYamlConfig() {
        return this.config.get();
    }


    private void loadVariables(YamlConfiguration configuration) {
        this.chance = configuration.getDouble("tokens.breaking.chance");
        this.minAmount = configuration.getLong("tokens.breaking.min");
        this.maxAmount = configuration.getLong("tokens.breaking.max");

        this.commandCooldown = configuration.getLong("tokens-command-cooldown");

        this.luckyBlockRewards = new HashMap<>();

        for (String key : configuration.getConfigurationSection("lucky-blocks").getKeys(false)) {
            CompMaterial material = CompMaterial.fromString(key);
            List<String> rewards = configuration.getStringList("lucky-blocks." + key);
            if (rewards.isEmpty()) {
                continue;
            }
            this.luckyBlockRewards.put(material.toMaterial(), rewards);
        }

        this.topPlayersAmount = configuration.getInt("top_players_amount");
        this.tokensTopFormat = configuration.getStringList("tokens-top-format");
        this.blocksTopFormat = configuration.getStringList("blocks-top-format");
        this.blocksTopFormatWeekly = configuration.getStringList("blocks-top-weekly-format");
        this.nextResetWeekly = configuration.getLong("next-reset-weekly");
        this.displayTokenMessages = configuration.getBoolean("display-token-messages");
        this.savePlayerDataInterval = configuration.getInt("player_data_save_interval");
        this.tokenItemDisplayName = configuration.getString("tokens.item.name");
        this.tokenItemLore = configuration.getStringList("tokens.item.lore");
        this.tokenItem = CompMaterial.fromString(configuration.getString("tokens.item.material")).toItem();
        this.startingTokens = configuration.getLong("starting-tokens");
        this.tokensCommandAliases = configuration.getStringList("tokens-command-aliases").toArray(new String[0]);
        this.tokensTopCommandAliases = configuration.getStringList("tokens-top-command-aliases").toArray(new String[0]);
        this.blocksTopCommandAliases = configuration.getStringList("blocks-top-command-aliases").toArray(new String[0]);

    }

    private void loadMessages(YamlConfiguration configuration) {
        this.messages = new HashMap<>();

        for (String key : configuration.getConfigurationSection("messages").getKeys(false)) {
            messages.put(key.toLowerCase(), TextUtils.applyColor(getConfig().get().getString("messages." + key)));
        }
    }

    public void reload() {
        this.config.reload();
        YamlConfiguration configuration = getYamlConfig();
        this.loadVariables(configuration);
        this.loadMessages(configuration);
    }

    public void load() {
        this.reload();
    }

    public String getMessage(String messageKey) {
        return this.messages.getOrDefault(messageKey.toLowerCase(), "Missing message with key: " + messageKey);
    }

    public Material getTokenItemMaterial() {
        return this.tokenItem.getType();
    }

    public List<String> getLuckyBlockReward(Material m) {
        return this.luckyBlockRewards.getOrDefault(m, new ArrayList<>());
    }

    public void setNextResetWeekly(long time) {
        this.nextResetWeekly = time;

    }

    public void save() {
        this.config.save();
    }

}
