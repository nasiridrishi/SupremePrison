package net.primegames.supremeprison.prestiges;

import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.prestiges.api.SupremePrisonPrestigesAPI;
import net.primegames.supremeprison.prestiges.api.SupremePrisonPrestigesAPIImpl;
import net.primegames.supremeprison.prestiges.commands.MaxPrestigeCommand;
import net.primegames.supremeprison.prestiges.commands.PrestigeAdminCommand;
import net.primegames.supremeprison.prestiges.commands.PrestigeCommand;
import net.primegames.supremeprison.prestiges.commands.PrestigeTopCommand;
import net.primegames.supremeprison.prestiges.config.PrestigeConfig;
import net.primegames.supremeprison.prestiges.listener.PrestigeListener;
import net.primegames.supremeprison.prestiges.manager.PrestigeManager;
import net.primegames.supremeprison.prestiges.repo.PrestigeRepository;
import net.primegames.supremeprison.prestiges.repo.impl.PrestigeRepositoryImpl;
import net.primegames.supremeprison.prestiges.service.PrestigeService;
import net.primegames.supremeprison.prestiges.service.impl.PrestigeServiceImpl;
import net.primegames.supremeprison.prestiges.task.SavePlayerDataTask;
import lombok.Getter;

@Getter
public final class SupremePrisonPrestiges implements SupremePrisonModule {

    public static final String MODULE_NAME = "Prestiges";
    private final SupremePrison core;
    @Getter
    private PrestigeConfig prestigeConfig;
    private PrestigeManager prestigeManager;
    @Getter
    private SupremePrisonPrestigesAPI api;
    private SavePlayerDataTask savePlayerDataTask;
    @Getter
    private PrestigeRepository prestigeRepository;

    @Getter
    private PrestigeService prestigeService;

    private boolean enabled;

    public SupremePrisonPrestiges(SupremePrison core) {
        this.core = core;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void reload() {
        this.prestigeConfig.reload();
    }

    @Override
    public void enable() {
        this.enabled = true;

        this.prestigeConfig = new PrestigeConfig(this);
        this.prestigeConfig.load();

        this.prestigeRepository = new PrestigeRepositoryImpl(this.core.getPluginDatabase());
        this.prestigeRepository.createTables();

        this.prestigeService = new PrestigeServiceImpl(this.prestigeRepository);

        this.prestigeManager = new PrestigeManager(this);
        this.prestigeManager.enable();

        this.api = new SupremePrisonPrestigesAPIImpl(this);

        this.savePlayerDataTask = new SavePlayerDataTask(this);
        this.savePlayerDataTask.start();

        this.registerCommands();
        this.registerListeners();
    }


    @Override
    public void disable() {
        this.savePlayerDataTask.stop();
        this.prestigeManager.disable();
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
        this.prestigeRepository.clearTableData();
    }

    private void registerCommands() {
        new PrestigeCommand(this).register();
        new MaxPrestigeCommand(this).register();
        new PrestigeTopCommand(this).register();
        new PrestigeAdminCommand(this).register();
    }

    private void registerListeners() {
        new PrestigeListener(this).register();
    }
}
