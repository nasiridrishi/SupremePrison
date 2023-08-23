package net.primegames.supremeprison.gangs;

import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.gangs.api.SupremePrisonGangsAPI;
import net.primegames.supremeprison.gangs.api.SupremePrisonGangsAPIImpl;
import net.primegames.supremeprison.gangs.commands.GangCommand;
import net.primegames.supremeprison.gangs.config.GangsConfig;
import net.primegames.supremeprison.gangs.listener.GangsListener;
import net.primegames.supremeprison.gangs.managers.GangsManager;
import net.primegames.supremeprison.gangs.model.GangTopByValueProvider;
import net.primegames.supremeprison.gangs.model.GangTopProvider;
import net.primegames.supremeprison.gangs.model.GangUpdateTopTask;
import net.primegames.supremeprison.gangs.repo.GangsRepository;
import net.primegames.supremeprison.gangs.repo.impl.GangsRepositoryImpl;
import net.primegames.supremeprison.gangs.service.GangsService;
import net.primegames.supremeprison.gangs.service.impl.GangsServiceImpl;
import lombok.Getter;

@Getter
public final class SupremePrisonGangs implements SupremePrisonModule {

    public static final String MODULE_NAME = "Gangs";

    @Getter
    private static SupremePrisonGangs instance;
    private final SupremePrison core;
    @Getter
    private SupremePrisonGangsAPI api;
    @Getter
    private GangsConfig config;
    @Getter
    private GangsManager gangsManager;
    @Getter
    private GangTopProvider gangTopProvider;
    @Getter
    private GangUpdateTopTask gangUpdateTopTask;
    @Getter
    private GangsRepository gangsRepository;

    @Getter
    private GangsService gangsService;

    private boolean enabled;

    public SupremePrisonGangs(SupremePrison prisonCore) {
        instance = this;
        this.core = prisonCore;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void reload() {
        this.config.reload();
    }

    @Override
    public void enable() {
        this.config = new GangsConfig(this);
        this.config.load();

        GangCommand gangCommand = new GangCommand(this);
        gangCommand.register();

        this.gangsRepository = new GangsRepositoryImpl(this.core.getPluginDatabase());
        this.gangsRepository.createTables();

        this.gangsService = new GangsServiceImpl(this.gangsRepository);

        this.gangsManager = new GangsManager(this);
        this.gangsManager.enable();

        this.gangTopProvider = new GangTopByValueProvider(this.gangsManager);

        GangsListener gangsListener = new GangsListener(this);
        gangsListener.register();

        this.gangUpdateTopTask = new GangUpdateTopTask(this, this.gangTopProvider);
        this.gangUpdateTopTask.start();


        this.api = new SupremePrisonGangsAPIImpl(this.gangsManager);

        this.enabled = true;
    }


    @Override
    public void disable() {
        this.gangsManager.disable();
        this.gangUpdateTopTask.stop();
        this.enabled = false;
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
        this.gangsRepository.clearTableData();
    }
}
