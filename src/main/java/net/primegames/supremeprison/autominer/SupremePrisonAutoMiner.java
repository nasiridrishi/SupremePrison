package net.primegames.supremeprison.autominer;

import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.autominer.api.SupremePrisonAutoMinerAPI;
import net.primegames.supremeprison.autominer.api.SupremePrisonAutoMinerAPIImpl;
import net.primegames.supremeprison.autominer.command.AdminAutoMinerCommand;
import net.primegames.supremeprison.autominer.command.AutoMinerCommand;
import net.primegames.supremeprison.autominer.config.AutoMinerConfig;
import net.primegames.supremeprison.autominer.listener.AutoMinerListener;
import net.primegames.supremeprison.autominer.manager.AutoMinerManager;
import net.primegames.supremeprison.autominer.repo.AutominerRepository;
import net.primegames.supremeprison.autominer.repo.impl.AutominerRepositoryImpl;
import net.primegames.supremeprison.autominer.service.AutominerService;
import net.primegames.supremeprison.autominer.service.impl.AutominerServiceImpl;
import lombok.Getter;

@Getter
public final class SupremePrisonAutoMiner implements SupremePrisonModule {

    public static final String MODULE_NAME = "Auto Miner";

    @Getter
    private static SupremePrisonAutoMiner instance;

    private final SupremePrison core;

    @Getter
    private AutoMinerManager manager;

    @Getter
    private AutoMinerConfig autoMinerConfig;

    @Getter
    private SupremePrisonAutoMinerAPI api;

    @Getter
    private AutominerService autominerService;

    @Getter
    private AutominerRepository autominerRepository;

    private boolean enabled;

    public SupremePrisonAutoMiner(SupremePrison core) {
        this.core = core;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void enable() {
        instance = this;

        this.autoMinerConfig = new AutoMinerConfig(this);
        this.autoMinerConfig.load();

        this.autominerRepository = new AutominerRepositoryImpl(this.core.getPluginDatabase());
        this.autominerRepository.createTables();
        this.autominerRepository.removeExpiredAutoMiners();

        this.autominerService = new AutominerServiceImpl(this.autominerRepository);

        this.manager = new AutoMinerManager(this);
        this.manager.load();

        AutoMinerListener listener = new AutoMinerListener(this);
        listener.subscribeToEvents();

        this.registerCommands();

        this.api = new SupremePrisonAutoMinerAPIImpl(this);

        this.enabled = true;
    }

    @Override
    public void disable() {
        this.manager.disable();
        this.enabled = false;
    }

    @Override
    public void reload() {
        this.autoMinerConfig.reload();
        this.manager.reload();
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public boolean isHistoryEnabled() {
        return true;
    }

    @Override
    public void resetPlayerData() {
        this.autominerRepository.clearTableData();
    }

    private void registerCommands() {
        AutoMinerCommand autoMinerCommand = new AutoMinerCommand(this);
        autoMinerCommand.register();

        AdminAutoMinerCommand adminAutoMinerCommand = new AdminAutoMinerCommand(this);
        adminAutoMinerCommand.register();
    }
}
