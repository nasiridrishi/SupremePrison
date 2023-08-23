package dev.drawethree.xprison.tokens;

import dev.drawethree.xprison.XPrison;
import dev.drawethree.xprison.XPrisonModule;
import dev.drawethree.xprison.tokens.api.XPrisonTokensAPI;
import dev.drawethree.xprison.tokens.api.XPrisonTokensAPIImpl;
import dev.drawethree.xprison.tokens.config.BlockRewardsConfig;
import dev.drawethree.xprison.tokens.config.TokensConfig;
import dev.drawethree.xprison.tokens.listener.TokensListener;
import dev.drawethree.xprison.tokens.managers.TokensManager;
import dev.drawethree.xprison.tokens.repo.BlocksRepository;
import dev.drawethree.xprison.tokens.repo.impl.BlocksRepositoryImpl;
import dev.drawethree.xprison.tokens.service.BlocksService;
import dev.drawethree.xprison.tokens.service.impl.BlocksServiceImpl;
import dev.drawethree.xprison.tokens.task.SavePlayerDataTask;
import lombok.Getter;

@Getter
@Getter
public final class XPrisonTokens implements XPrisonModule {

    public static final String MODULE_NAME = "Tokens";

    private static XPrisonTokens instance;
    private final XPrison core;
    @Getter
    private BlockRewardsConfig blockRewardsConfig;
    @Getter
    private TokensConfig tokensConfig;
    @Getter
    private XPrisonTokensAPI api;
    @Getter
    private TokensManager tokensManager;
    @Getter
    private BlocksRepository blocksRepository;
    @Getter
    private BlocksService blocksService;
    private SavePlayerDataTask savePlayerDataTask;

    private boolean enabled;


    public XPrisonTokens(XPrison prisonCore) {
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

        this.api = new XPrisonTokensAPIImpl(this.tokensManager);

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
