package net.primegames.supremeprison;

import com.github.lalyos.jfiglet.FigletFont;
import net.primegames.supremeprison.autominer.SupremePrisonAutoMiner;
import net.primegames.supremeprison.autosell.SupremePrisonAutoSell;
import net.primegames.supremeprison.config.FileManager;
import net.primegames.supremeprison.database.SQLDatabase;
import net.primegames.supremeprison.database.impl.MySQLDatabase;
import net.primegames.supremeprison.database.impl.SQLiteDatabase;
import net.primegames.supremeprison.database.model.ConnectionProperties;
import net.primegames.supremeprison.database.model.DatabaseCredentials;
import net.primegames.supremeprison.economy.PrisonEconomyManager;
import net.primegames.supremeprison.enchants.SupremePrisonEnchants;
import net.primegames.supremeprison.gangs.SupremePrisonGangs;
import net.primegames.supremeprison.history.SupremePrisonHistory;
import net.primegames.supremeprison.mainmenu.MainMenu;
import net.primegames.supremeprison.mainmenu.help.HelpGui;
import net.primegames.supremeprison.mines.SupremePrisonMines;
import net.primegames.supremeprison.multipliers.SupremePrisonMultipliers;
import net.primegames.supremeprison.nicknames.repo.NicknameRepository;
import net.primegames.supremeprison.nicknames.repo.impl.NicknameRepositoryImpl;
import net.primegames.supremeprison.nicknames.service.NicknameService;
import net.primegames.supremeprison.nicknames.service.impl.NicknameServiceImpl;
import net.primegames.supremeprison.pickaxelevels.SupremePrisonPickaxeLevels;
import net.primegames.supremeprison.placeholders.SupremePrisonPAPIPlaceholder;
import net.primegames.supremeprison.prestiges.SupremePrisonPrestiges;
import net.primegames.supremeprison.ranks.SupremePrisonRanks;
import net.primegames.supremeprison.tokens.SupremePrisonTokens;
import net.primegames.supremeprison.utils.Constants;
import net.primegames.supremeprison.utils.compat.CompMaterial;
import net.primegames.supremeprison.utils.misc.SkullUtils;
import net.primegames.supremeprison.utils.text.TextUtils;
import lombok.Getter;
import me.lucko.helper.Commands;
import me.lucko.helper.Events;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.flag.WrappedState;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public final class SupremePrison extends ExtendedJavaPlugin {

    @Getter
    private static SupremePrison instance;

    @Getter
    private boolean debugMode;
    private Map<String, SupremePrisonModule> modules;
    private SQLDatabase pluginDatabase;
    private Economy economy;
    private FileManager fileManager;
    private SupremePrisonRanks ranks;
    private SupremePrisonPrestiges prestiges;
    private SupremePrisonMultipliers multipliers;
    private SupremePrisonEnchants enchants;
    private SupremePrisonAutoMiner autoMiner;
    private SupremePrisonPickaxeLevels pickaxeLevels;
    private SupremePrisonGangs gangs;
    private SupremePrisonMines mines;
    private SupremePrisonAutoSell autoSell;
    private SupremePrisonHistory history;
    @Getter
    private SupremePrisonTokens tokens;
    private PrisonEconomyManager economyManager;


    private List<Material> supportedPickaxes;

    private NicknameService nicknameService;

    @Override
    protected void load() {
        instance = this;
        registerWGFlag();
    }

    @Override
    protected void enable() {

        this.printOnEnableMessage();
        this.modules = new LinkedHashMap<>();
        this.fileManager = new FileManager(this);
        this.fileManager.getConfig("config.yml").copyDefaults(true).save();
        this.debugMode = this.getConfig().getBoolean("debug-mode", false);

        if (!this.initDatabase()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!this.setupEconomy()) {
            this.getLogger().warning("Economy provider for Vault not found! Economy provider is strictly required. Disabling plugin...");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        } else {
            this.getLogger().info("Economy provider for Vault found - " + this.getEconomy().getName());
        }

        this.initVariables();
        this.initModules();
        this.loadModules();

        this.initNicknameService();

        this.registerPlaceholders();

        this.registerMainEvents();
        this.registerMainCommand();

        SkullUtils.init();
    }

    private void printOnEnableMessage() {
        try {
            this.getLogger().info(FigletFont.convertOneLine("X-PRISON"));
            this.getLogger().info(this.getDescription().getVersion());
            this.getLogger().info("By: " + this.getDescription().getAuthors());
            this.getLogger().info("Website: " + this.getDescription().getWebsite());
        } catch (IOException ignored) {
        }
    }

    private void initNicknameService() {
        NicknameRepository nicknameRepository = new NicknameRepositoryImpl(this.getPluginDatabase());
        nicknameRepository.createTables();
        this.nicknameService = new NicknameServiceImpl(nicknameRepository);
    }

    private void initVariables() {
        this.supportedPickaxes = this.getConfig().getStringList("supported-pickaxes").stream().map(CompMaterial::fromString).map(CompMaterial::getMaterial).collect(Collectors.toList());

        for (Material m : this.supportedPickaxes) {
            this.getLogger().info("Added support for pickaxe: " + m);
        }
    }

    private void loadModules() {
        if (this.getConfig().getBoolean("modules.prestiges")) {
            this.loadModule(prestiges);
        }

        if (this.getConfig().getBoolean("modules.multipliers")) {
            this.loadModule(multipliers);
        }

        if (this.getConfig().getBoolean("modules.mines")) {
            this.loadModule(mines);
        }

        if (this.getConfig().getBoolean("modules.ranks")) {
            this.loadModule(ranks);
        }

        if (this.getConfig().getBoolean("modules.autosell")) {
//            if (isUltraBackpacksEnabled()) {
//                this.getLogger().info("Module AutoSell will not be loaded because selling system is handled by UltraBackpacks.");
//            } else {
//                this.loadModule(autoSell);
//            }
            this.loadModule(autoSell);
        }

        if (this.getConfig().getBoolean("modules.enchants")) {
            this.loadModule(enchants);
        }
        if (this.getConfig().getBoolean("modules.autominer")) {
            this.loadModule(autoMiner);
        }
        if (this.getConfig().getBoolean("modules.gangs")) {
            this.loadModule(gangs);
        }
        if (this.getConfig().getBoolean("modules.pickaxe_levels")) {
            if (!this.isModuleEnabled("Enchants")) {
                this.getLogger().warning(TextUtils.applyColor("&cX-Prison - Module 'Pickaxe Levels' requires to have enchants module enabled."));
            } else {
                this.loadModule(pickaxeLevels);
            }
        }
        if (this.getConfig().getBoolean("modules.history")) {
            this.loadModule(history);
        }
        this.loadModule(economyManager);
        this.loadModule(tokens);
    }

    private boolean initDatabase() {
        try {
            String databaseType = this.getConfig().getString("database_type");
            ConnectionProperties connectionProperties = ConnectionProperties.fromConfig(this.getConfig());

            if ("sqlite".equalsIgnoreCase(databaseType)) {
                this.pluginDatabase = new SQLiteDatabase(this, connectionProperties);
                this.getLogger().info("Using SQLite (local) database.");
            } else if ("mysql".equalsIgnoreCase(databaseType)) {
                DatabaseCredentials credentials = DatabaseCredentials.fromConfig(this.getConfig());
                this.pluginDatabase = new MySQLDatabase(this, credentials, connectionProperties);
                this.getLogger().info("Using MySQL (remote) database.");
            } else {
                this.getLogger().warning(String.format("Error! Unknown database type: %s. Disabling plugin.", databaseType));
                this.getServer().getPluginManager().disablePlugin(this);
                return false;
            }

            this.pluginDatabase.connect();
        } catch (Exception e) {
            this.getLogger().warning("Could not maintain Database Connection. Disabling plugin.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void initModules() {
        this.multipliers = new SupremePrisonMultipliers(this);
        this.enchants = new SupremePrisonEnchants(this);
        this.autoSell = new SupremePrisonAutoSell(this);
        this.autoMiner = new SupremePrisonAutoMiner(this);
        this.pickaxeLevels = new SupremePrisonPickaxeLevels(this);
        this.gangs = new SupremePrisonGangs(this);
        this.mines = new SupremePrisonMines(this);
        this.ranks = new SupremePrisonRanks(this);
        this.prestiges = new SupremePrisonPrestiges(this);
        this.history = new SupremePrisonHistory(this);
        this.economyManager = new PrisonEconomyManager();
        this.tokens = new SupremePrisonTokens(this);
        this.modules.put(this.multipliers.getName().toLowerCase(), this.multipliers);
        this.modules.put(this.enchants.getName().toLowerCase(), this.enchants);
        this.modules.put(this.autoSell.getName().toLowerCase(), this.autoSell);
        this.modules.put(this.autoMiner.getName().toLowerCase(), this.autoMiner);
        this.modules.put(this.pickaxeLevels.getName().toLowerCase(), this.pickaxeLevels);
        this.modules.put(this.gangs.getName().toLowerCase(), this.gangs);
        this.modules.put(this.mines.getName().toLowerCase(), this.mines);
        this.modules.put(this.ranks.getName().toLowerCase(), this.ranks);
        this.modules.put(this.prestiges.getName().toLowerCase(), this.prestiges);
        this.modules.put(this.history.getName().toLowerCase(), this.history);
        this.modules.put(this.economyManager.getName().toLowerCase(), this.economyManager);
        this.modules.put(this.tokens.getName().toLowerCase(), this.tokens);
    }

    private void registerMainEvents() {
        //Updating of mapping table
        Events.subscribe(PlayerJoinEvent.class, EventPriority.LOW)
                .handler(e -> this.nicknameService.updatePlayerNickname(e.getPlayer())).bindWith(this);
    }

    private void loadModule(SupremePrisonModule module) {
        if (module.isEnabled()) {
            return;
        }
        module.enable();
        this.getLogger().info(TextUtils.applyColor(String.format("&aX-Prison - Module %s loaded.", module.getName())));
    }

    //Always unload via iterator!
    private void unloadModule(SupremePrisonModule module) {
        if (!module.isEnabled()) {
            return;
        }
        module.disable();
        this.getLogger().info(TextUtils.applyColor(String.format("&aX-Prison - Module %s unloaded.", module.getName())));
    }

    public void debug(String msg, SupremePrisonModule module) {
        if (!this.debugMode) {
            return;
        }
        if (module != null) {
            this.getLogger().info(String.format("[%s] %s", module.getName(), TextUtils.applyColor(msg)));
        } else {
            this.getLogger().info(TextUtils.applyColor(msg));
        }
    }

    public void reloadModule(SupremePrisonModule module) {
        if (!module.isEnabled()) {
            return;
        }
        module.reload();
        this.getLogger().info(TextUtils.applyColor(String.format("X-Prison - Module %s reloaded.", module.getName())));
    }

    private void registerMainCommand() {

        List<String> commandAliases = this.getConfig().getStringList("main-command-aliases");
        String[] commandAliasesArray = commandAliases.toArray(new String[commandAliases.size()]);

        Commands.create()
                .assertPermission("supremeprison.admin")
                .handler(c -> {
                    if (c.args().size() == 0 && c.sender() instanceof Player) {
                        new MainMenu(this, (Player) c.sender()).open();
                    } else if (c.args().size() == 1 && c.sender() instanceof Player && "help".equalsIgnoreCase(c.rawArg(0)) || "?".equalsIgnoreCase(c.rawArg(0))) {
                        new HelpGui((Player) c.sender()).open();
                    }
                }).registerAndBind(this, commandAliasesArray);
    }

    @Override
    protected void disable() {
        Iterator<SupremePrisonModule> it = this.modules.values().iterator();
        while (it.hasNext()) {
            this.unloadModule(it.next());
            it.remove();
        }
        if (this.pluginDatabase != null) {
            if (this.pluginDatabase instanceof SQLDatabase) {
                SQLDatabase sqlDatabase = this.pluginDatabase;
                sqlDatabase.close();
            }
        }
    }

    public boolean isModuleEnabled(String moduleName) {
        SupremePrisonModule module = this.modules.get(moduleName.toLowerCase());
        return module != null && module.isEnabled();
    }

    private void registerPlaceholders() {
        if (isPlaceholderAPIEnabled()) {
            new SupremePrisonPAPIPlaceholder(this).register();
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public boolean isPickaxeSupported(Material m) {
        return this.supportedPickaxes.contains(m);
    }

    public boolean isPickaxeSupported(ItemStack item) {
        return item != null && isPickaxeSupported(item.getType());
    }

    public Collection<SupremePrisonModule> getModules() {
        return this.modules.values();
    }

    public void setDebugMode(boolean enabled) {
        this.debugMode = enabled;
        this.getConfig().set("debug-mode", debugMode);
        this.saveConfig();
    }

    public boolean isUltraBackpacksEnabled() {
        return this.getServer().getPluginManager().isPluginEnabled("UltraBackpacks");
    }

    public boolean isPlaceholderAPIEnabled() {
        return this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public boolean isMVdWPlaceholderAPIEnabled() {
        return this.getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI");
    }

    private void registerWGFlag() {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null) {
            return;
        }
        try {
            getWorldGuardWrapper().registerFlag(Constants.ENCHANTS_WG_FLAG_NAME, WrappedState.class, WrappedState.DENY);
        } catch (IllegalStateException e) {
            // This happens during plugin reloads. Flag cannot be registered as WG was already loaded,
            // so we can safely ignore this exception.
        }
    }

    public WorldGuardWrapper getWorldGuardWrapper() {
        return WorldGuardWrapper.getInstance();
    }

}
