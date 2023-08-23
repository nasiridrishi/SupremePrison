package net.primegames.supremeprison.pickaxelevels;

import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.pickaxelevels.api.SupremePrisonPickaxeLevelsAPI;
import net.primegames.supremeprison.pickaxelevels.api.SupremePrisonPickaxeLevelsAPIImpl;
import net.primegames.supremeprison.pickaxelevels.config.PickaxeLevelsConfig;
import net.primegames.supremeprison.pickaxelevels.listener.PickaxeLevelsListener;
import net.primegames.supremeprison.pickaxelevels.manager.PickaxeLevelsManager;
import lombok.Getter;

@Getter
public final class SupremePrisonPickaxeLevels implements SupremePrisonModule {

    public static final String MODULE_NAME = "Pickaxe Levels";
    private final SupremePrison core;
    @Getter
    private PickaxeLevelsConfig pickaxeLevelsConfig;
    @Getter
    private PickaxeLevelsManager pickaxeLevelsManager;
    @Getter
    private SupremePrisonPickaxeLevelsAPI api;
    private boolean enabled;

    public SupremePrisonPickaxeLevels(SupremePrison core) {
        this.core = core;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void reload() {
        this.pickaxeLevelsConfig.reload();
    }

    @Override
    public void enable() {
        this.pickaxeLevelsConfig = new PickaxeLevelsConfig(this);
        this.pickaxeLevelsConfig.load();
        this.pickaxeLevelsManager = new PickaxeLevelsManager(this);

        this.registerListeners();

        this.api = new SupremePrisonPickaxeLevelsAPIImpl(this.pickaxeLevelsManager);
        this.enabled = true;
    }

    private void registerListeners() {
        new PickaxeLevelsListener(this).register();
    }

    @Override
    public void disable() {
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
