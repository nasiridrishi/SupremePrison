package net.primegames.supremeprison.enchants;

import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.autosell.SupremePrisonAutoSell;
import net.primegames.supremeprison.enchants.api.SupremePrisonEnchantsAPI;
import net.primegames.supremeprison.enchants.api.SupremePrisonEnchantsAPIImpl;
import net.primegames.supremeprison.enchants.config.EnchantsConfig;
import net.primegames.supremeprison.enchants.gui.DisenchantGUI;
import net.primegames.supremeprison.enchants.gui.EnchantGUI;
import net.primegames.supremeprison.enchants.listener.EnchantsListener;
import net.primegames.supremeprison.enchants.managers.CooldownManager;
import net.primegames.supremeprison.enchants.managers.EnchantsManager;
import net.primegames.supremeprison.enchants.managers.RespawnManager;
import net.primegames.supremeprison.enchants.repo.EnchantsRepository;
import net.primegames.supremeprison.mines.SupremePrisonMines;
import net.primegames.supremeprison.multipliers.SupremePrisonMultipliers;
import lombok.Getter;
import me.lucko.helper.utils.Players;
import net.primegames.supremeprison.enchants.command.*;
import org.bukkit.entity.Player;

@Getter
public final class SupremePrisonEnchants implements SupremePrisonModule {


    public static final String MODULE_NAME = "Enchants";

    @Getter
    private static SupremePrisonEnchants instance;
    @Getter
    private final SupremePrison core;
    @Getter
    private SupremePrisonEnchantsAPI api;
    @Getter
    private EnchantsManager enchantsManager;
    @Getter
    private CooldownManager cooldownManager;
    @Getter
    private RespawnManager respawnManager;
    @Getter
    private EnchantsConfig enchantsConfig;
    @Getter
    private EnchantsRepository enchantsRepository;
    private boolean enabled;

    public SupremePrisonEnchants(SupremePrison core) {
        instance = this;
        this.core = core;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void reload() {

        this.enchantsConfig.reload();
        this.enchantsRepository.reload();

        EnchantGUI.init();
        DisenchantGUI.init();

    }

    @Override
    public void enable() {

        this.enchantsConfig = new EnchantsConfig(this);
        this.enchantsConfig.load();

        this.cooldownManager = new CooldownManager(this);
        this.respawnManager = new RespawnManager(this);

        this.enchantsManager = new EnchantsManager(this);
        this.enchantsManager.enable();

        EnchantsListener listener = new EnchantsListener(this);
        listener.register();

        this.registerCommands();

        this.enchantsRepository = new EnchantsRepository(this);
        this.enchantsRepository.loadDefaultEnchantments();

        EnchantGUI.init();
        DisenchantGUI.init();

        this.api = new SupremePrisonEnchantsAPIImpl(this.enchantsManager, this.enchantsRepository);


        this.enabled = true;
    }


    private void registerCommands() {
        DisenchantCommand disenchantCommand = new DisenchantCommand(this);
        disenchantCommand.register();

        EnchantMenuCommand enchantMenuCommand = new EnchantMenuCommand(this);
        enchantMenuCommand.register();

        GiveFirstJoinPickaxeCommand giveFirstJoinPickaxeCommand = new GiveFirstJoinPickaxeCommand(this);
        giveFirstJoinPickaxeCommand.register();

        GivePickaxeCommand givePickaxeCommand = new GivePickaxeCommand(this);
        givePickaxeCommand.register();

        ValueCommand valueCommand = new ValueCommand(this);
        valueCommand.register();
    }


    @Override
    public void disable() {
        for (Player p : Players.all()) {
            p.closeInventory();
        }
        this.enchantsManager.disable();
        this.enabled = false;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public boolean isHistoryEnabled() {
        return false;
    }

    @Override
    public void resetPlayerData() {
    }

    public boolean isAutoSellModuleEnabled() {
        return this.core.isModuleEnabled(SupremePrisonAutoSell.MODULE_NAME);
    }

    public boolean isMultipliersModuleEnabled() {
        return this.core.isModuleEnabled(SupremePrisonMultipliers.MODULE_NAME);
    }

    public boolean isMinesModuleEnabled() {
        return this.core.isModuleEnabled(SupremePrisonMines.MODULE_NAME);
    }

}
