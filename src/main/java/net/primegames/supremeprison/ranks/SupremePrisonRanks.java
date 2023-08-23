package net.primegames.supremeprison.ranks;

import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.placeholders.SupremePrisonPapiRanksPlaceHolder;
import net.primegames.supremeprison.ranks.api.SupremePrisonRanksAPI;
import net.primegames.supremeprison.ranks.api.SupremePrisonRanksAPIImpl;
import net.primegames.supremeprison.ranks.commands.AutoMineTeleport;
import net.primegames.supremeprison.ranks.commands.RankupCommand;
import net.primegames.supremeprison.ranks.commands.SetRankCommand;
import net.primegames.supremeprison.ranks.config.RanksConfig;
import net.primegames.supremeprison.ranks.listener.RanksListener;
import net.primegames.supremeprison.ranks.manager.RanksManager;
import net.primegames.supremeprison.ranks.repo.RanksRepository;
import net.primegames.supremeprison.ranks.repo.impl.RanksRepositoryImpl;
import net.primegames.supremeprison.ranks.service.RanksService;
import net.primegames.supremeprison.ranks.service.impl.RanksServiceImpl;
import lombok.Getter;

@Getter
public final class SupremePrisonRanks implements SupremePrisonModule {

    public static final String MODULE_NAME = "Ranks";
    private final SupremePrison core;
    @Getter
    private RanksConfig ranksConfig;
    @Getter
    private RanksManager ranksManager;
    @Getter
    private SupremePrisonRanksAPI api;
    @Getter
    private RanksRepository ranksRepository;

    @Getter
    private RanksService ranksService;

    private boolean enabled;

    public SupremePrisonRanks(SupremePrison core) {
        this.core = core;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void reload() {
        this.ranksConfig.reload();
    }

    @Override
    public void enable() {
        this.enabled = true;
        this.ranksConfig = new RanksConfig(this);
        this.ranksConfig.load();
        this.ranksRepository = new RanksRepositoryImpl(this.core.getPluginDatabase());
        this.ranksRepository.createTables();
        this.ranksService = new RanksServiceImpl(this.ranksRepository);
        this.ranksManager = new RanksManager(this);
        this.ranksManager.enable();
        this.api = new SupremePrisonRanksAPIImpl(this.ranksManager);
        this.registerCommands();
        this.registerListeners();
        (new SupremePrisonPapiRanksPlaceHolder(this)).register();
    }

    private void registerListeners() {
        new RanksListener(this).register();
    }

    @Override
    public void disable() {
        this.ranksManager.disable();
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
        this.ranksRepository.clearTableData();
    }

    private void registerCommands() {
        new RankupCommand(this).register();
        //no thanks new MaxRankupCommand(this).register();
        new SetRankCommand(this).register();
        new AutoMineTeleport(this).register();
    }
}
