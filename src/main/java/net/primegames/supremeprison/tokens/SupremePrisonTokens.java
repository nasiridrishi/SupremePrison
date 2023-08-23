package net.primegames.supremeprison.tokens;

import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.tokens.api.SupremePrisonTokensAPI;
import net.primegames.supremeprison.tokens.api.SupremePrisonTokensAPIImpl;
import net.primegames.supremeprison.tokens.config.BlockRewardsConfig;
import net.primegames.supremeprison.tokens.config.TokensConfig;
import net.primegames.supremeprison.tokens.listener.TokensListener;
import net.primegames.supremeprison.tokens.managers.TokensManager;
import net.primegames.supremeprison.tokens.repo.BlocksRepository;
import net.primegames.supremeprison.tokens.repo.impl.BlocksRepositoryImpl;
import net.primegames.supremeprison.tokens.service.BlocksService;
import net.primegames.supremeprison.tokens.service.impl.BlocksServiceImpl;
import net.primegames.supremeprison.tokens.task.SavePlayerDataTask;
import lombok.Getter;

@Getter
public final class SupremePrisonTokens implements SupremePrisonModule {

    public static final String MODULE_NAME = "Tokens";

    @Getter
    private static SupremePrisonTokens instance;
    private final SupremePrison core;
    @Getter
    private BlockRewardsConfig blockRewardsConfig;
    @Getter
    private TokensConfig tokensConfig;
    @Getter
    private SupremePrisonTokensAPI api;
    @Getter
    private TokensManager tokensManager;
    @Getter
    private BlocksRepository blocksRepository;
    @Getter
    private BlocksService blocksService;
    private SavePlayerDataTask savePlayerDataTask;

    private boolean enabled;


    public SupremePrisonTokens(SupremePrison prisonCore) {
        instance = this;
        this.core = prisonCore;
    }


    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void reload() {
        this.tokensConfig.reload();
        this.blockRewardsConfig.reload();
        this.tokensManager.reload();
    }


    @Override
    public void enable() {

        this.tokensConfig = new TokensConfig(this);
        this.blockRewardsConfig = new BlockRewardsConfig(this);

        this.tokensConfig.load();
        this.blockRewardsConfig.load();


        this.blocksRepository = new BlocksRepositoryImpl(this.core.getPluginDatabase());
        this.blocksRepository.createTables();

        this.blocksService = new BlocksServiceImpl(this.blocksRepository);

        this.tokensManager = new TokensManager(this);
        this.tokensManager.enable();

        this.savePlayerDataTask = new SavePlayerDataTask(this);
        this.savePlayerDataTask.start();

        this.registerListeners();

        this.api = new SupremePrisonTokensAPIImpl(this.tokensManager);

        this.enabled = true;
    }

    private void registerListeners() {
        new TokensListener(this).subscribeToEvents();
    }


    @Override
    public void disable() {
        this.tokensManager.disable();

        this.savePlayerDataTask.stop();

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
        this.blocksRepository.clearTableData();
    }

}
