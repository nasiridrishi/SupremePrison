package net.primegames.supremeprison.autosell;

import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.autosell.api.SupremePrisonAutoSellAPI;
import net.primegames.supremeprison.autosell.api.SupremePrisonAutoSellAPIImpl;
import net.primegames.supremeprison.autosell.command.AutoSellCommand;
import net.primegames.supremeprison.autosell.command.SellAllCommand;
import net.primegames.supremeprison.autosell.command.SellPriceCommand;
import net.primegames.supremeprison.autosell.config.AutoSellConfig;
import net.primegames.supremeprison.autosell.listener.AutoSellListener;
import net.primegames.supremeprison.autosell.manager.AutoSellManager;
import net.primegames.supremeprison.autosell.model.AutoSellBroadcastTask;
import net.primegames.supremeprison.multipliers.SupremePrisonMultipliers;
import lombok.Getter;

@Getter
public final class SupremePrisonAutoSell implements SupremePrisonModule {

    public static final String MODULE_NAME = "Auto Sell";

    @Getter
    private static SupremePrisonAutoSell instance;
    @Getter
    private final SupremePrison core;
    @Getter
    private SupremePrisonAutoSellAPI api;
    @Getter
    private AutoSellConfig autoSellConfig;
    @Getter
    private AutoSellManager manager;
    @Getter
    private AutoSellBroadcastTask broadcastTask;

    private boolean enabled;

    public SupremePrisonAutoSell(SupremePrison core) {
        instance = this;
        this.core = core;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void reload() {
        this.autoSellConfig.reload();
        this.manager.reload();
    }


    @Override
    public void enable() {
        this.autoSellConfig = new AutoSellConfig(this);
        this.autoSellConfig.load();

        this.manager = new AutoSellManager(this);
        this.manager.load();

        this.broadcastTask = new AutoSellBroadcastTask(this);
        this.broadcastTask.start();

        AutoSellListener listener = new AutoSellListener(this);
        listener.subscribeToEvents();

        this.registerCommands();

        this.api = new SupremePrisonAutoSellAPIImpl(this);
        this.enabled = true;
    }

    private void registerCommands() {
        SellAllCommand sellAllCommand = new SellAllCommand(this);
        sellAllCommand.register();

        AutoSellCommand autoSellCommand = new AutoSellCommand(this);
        autoSellCommand.register();

        SellPriceCommand sellPriceCommand = new SellPriceCommand(this);
        sellPriceCommand.register();
    }

    public boolean isMultipliersModuleEnabled() {
        return this.core.isModuleEnabled(SupremePrisonMultipliers.MODULE_NAME);
    }

    @Override
    public void disable() {
        this.broadcastTask.stop();
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

}
