package net.primegames.supremeprison.ranks.config;

import net.primegames.supremeprison.config.FileManager;
import net.primegames.supremeprison.mines.model.mine.Mine;
import net.primegames.supremeprison.ranks.SupremePrisonRanks;
import net.primegames.supremeprison.ranks.model.Rank;
import net.primegames.supremeprison.utils.text.TextUtils;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class RanksConfig {

    private final SupremePrisonRanks plugin;
    private final FileManager.Config config;
    @Getter
    private final Map<Integer, Rank> ranksById;
    @Getter
    private Map<Integer, Rank> shortedRanksById = new HashMap<>();
    private Map<String, String> messages;
    @Getter
    private Rank defaultRank;
    @Getter
    private Rank maxRank;
    @Getter
    private boolean useTokensCurrency;
    @Getter
    private String progressBarDelimiter;
    @Getter
    private int progressBarLength;

    public RanksConfig(SupremePrisonRanks plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getCore().getFileManager().getConfig("ranks.yml").copyDefaults(true).save();
        this.ranksById = new HashMap<>();
    }

    private void loadMessages(YamlConfiguration configuration) {
        this.messages = new HashMap<>();

        for (String key : Objects.requireNonNull(configuration.getConfigurationSection("messages")).getKeys(false)) {
            messages.put(key.toLowerCase(), TextUtils.applyColor(getConfig().get().getString("messages." + key)));
        }
    }

    public void reload() {
        YamlConfiguration configuration = getYamlConfig();
        this.loadVariables(configuration);
        this.loadRanks(configuration);
        this.loadMessages(configuration);
    }

    private void loadRanks(YamlConfiguration configuration) {
        this.ranksById.clear();
        List<String> allRankUpCommands = configuration.getStringList("RankUpCommands");
        ConfigurationSection section = configuration.getConfigurationSection("Ranks");
        boolean defaultSet = false;
        if (section != null) {
            for (String key : section.getKeys(false)) {
                String rootPath = "Ranks." + key + ".";
                int id = Integer.parseInt(key);
                String prefix = TextUtils.applyColor(configuration.getString(rootPath + "Prefix"));
                String name = TextUtils.applyColor(configuration.getString(rootPath + "Name"));
                long cost = configuration.getLong(rootPath + "Cost");
                List<String> commands = configuration.getStringList(rootPath + "CMD");
                commands.addAll(allRankUpCommands);
                String mineName = configuration.getString(rootPath + "Mine", null);
                Mine mine = null;
                if (mineName != null) {
                    this.plugin.getCore().getLogger().info("Loading mine " + mineName + " for rank " + id);
                    mine = this.plugin.getCore().getMines().getManager().getMineByName(mineName);
                    if (mine == null) {
                        this.plugin.getCore().getLogger().severe("Couldn't find mine " + mineName + " for rank " + id + "! Skipping rank load...");
                        continue;
                    }
                }
                Rank rank;
                if (mine == null) {
                    this.plugin.getCore().getLogger().info("Registering rank " + id + " without mine");
                    rank = new Rank(id, cost, name, prefix, commands);
                } else {
                    rank = new Rank(id, cost, name, prefix, commands, mine);
                }
                this.ranksById.put(id, rank);
                if (!defaultSet) {
                    this.defaultRank = rank;
                    defaultSet = true;
                }
                this.maxRank = rank;
            }
        }
        this.plugin.getCore().getLogger().info(String.format("Loaded %d ranks!", ranksById.keySet().size()));

        //SHORT ranks by id

        /*
         * SHORT ranks by id from decreasing to increasing.
         */
        List<Map.Entry<Integer, Rank>> sortedEntries = new ArrayList<>(ranksById.entrySet());
        sortedEntries.sort((o1, o2) -> o2.getKey().compareTo(o1.getKey()));
        Map<Integer, Rank> shortedRanks = new HashMap<>();
        for (Map.Entry<Integer, Rank> entry : sortedEntries) {
            shortedRanks.put(entry.getKey(), entry.getValue());
        }
        this.shortedRanksById = shortedRanks;
    }

    public void load() {
        this.reload();
    }


    public String getMessage(String messageKey) {
        return this.messages.getOrDefault(messageKey.toLowerCase(), "Missing message with key: " + messageKey);
    }

    private void loadVariables(YamlConfiguration configuration) {
        this.useTokensCurrency = configuration.getBoolean("use_tokens_currency");
        this.progressBarDelimiter = configuration.getString("progress-bar-delimiter");
        this.progressBarLength = configuration.getInt("progress-bar-length");
        this.plugin.getCore().getLogger().info("Using " + (useTokensCurrency ? "Tokens" : "Money") + " currency for Ranks.");
    }

    private FileManager.Config getConfig() {
        return this.config;
    }

    public YamlConfiguration getYamlConfig() {
        return this.config.get();
    }

    public Rank getRankById(int id) {
        return this.ranksById.get(id);
    }

    public Rank getByName(String name) {
        for (Rank rank : ranksById.values()) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

}
