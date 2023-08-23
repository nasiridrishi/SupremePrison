package net.primegames.supremeprison.history;

import net.primegames.supremeprison.SupremePrison;
import net.primegames.supremeprison.SupremePrisonModule;
import net.primegames.supremeprison.autominer.api.events.PlayerAutoMinerTimeModifyEvent;
import net.primegames.supremeprison.autominer.api.events.PlayerAutomineEvent;
import net.primegames.supremeprison.gangs.api.events.GangCreateEvent;
import net.primegames.supremeprison.gangs.api.events.GangDisbandEvent;
import net.primegames.supremeprison.gangs.api.events.GangJoinEvent;
import net.primegames.supremeprison.gangs.api.events.GangLeaveEvent;
import net.primegames.supremeprison.history.api.SupremePrisonHistoryAPI;
import net.primegames.supremeprison.history.api.SupremePrisonHistoryAPIImpl;
import net.primegames.supremeprison.history.gui.PlayerHistoryGUI;
import net.primegames.supremeprison.history.manager.HistoryManager;
import net.primegames.supremeprison.history.repo.HistoryRepository;
import net.primegames.supremeprison.history.repo.impl.HistoryRepositoryImpl;
import net.primegames.supremeprison.history.service.HistoryService;
import net.primegames.supremeprison.history.service.impl.HistoryServiceImpl;
import net.primegames.supremeprison.multipliers.api.events.PlayerMultiplierReceiveEvent;
import net.primegames.supremeprison.prestiges.api.events.PlayerPrestigeEvent;
import net.primegames.supremeprison.ranks.api.events.PlayerRankUpEvent;
import net.primegames.supremeprison.tokens.api.events.PlayerTokensLostEvent;
import net.primegames.supremeprison.tokens.api.events.PlayerTokensReceiveEvent;
import net.primegames.supremeprison.utils.misc.TimeUtil;
import net.primegames.supremeprison.utils.player.PlayerUtils;
import lombok.Getter;
import me.lucko.helper.Commands;
import me.lucko.helper.Events;
import me.lucko.helper.event.filter.EventFilters;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;

@Getter
public final class SupremePrisonHistory implements SupremePrisonModule {

    private static final String MODULE_NAME = "History";
    private final SupremePrison core;

    @Getter
    private HistoryManager historyManager;

    @Getter
    private HistoryRepository historyRepository;

    @Getter
    private HistoryService historyService;

    @Getter
    private SupremePrisonHistoryAPI api;

    private boolean enabled;

    public SupremePrisonHistory(SupremePrison core) {
        this.core = core;
        this.enabled = false;
    }

    @Override
    public void enable() {
        this.enabled = true;
        this.historyRepository = new HistoryRepositoryImpl(this.core.getPluginDatabase());
        this.historyRepository.createTables();
        this.historyService = new HistoryServiceImpl(this.historyRepository);
        this.historyManager = new HistoryManager(this);
        this.api = new SupremePrisonHistoryAPIImpl(this);
        this.registerCommands();
        this.registerEvents();
    }

    private void registerEvents() {
        Events.subscribe(PlayerTokensReceiveEvent.class, EventPriority.MONITOR)
                .filter(EventFilters.ignoreCancelled())
                .handler(e -> {
                    this.historyManager.createPlayerHistoryLine(e.getPlayer(), this.core.getEconomyManager(), String.format("&a&l+%,d TOKENS &f(%s).&7Current Tokens: &e%,d", e.getAmount(), e.getCause().name(), this.core.getTokens().getApi().getPlayerTokens(e.getPlayer())));
                }).bindWith(this.core);
        Events.subscribe(PlayerTokensLostEvent.class, EventPriority.MONITOR)
                .handler(e -> {
                    this.historyManager.createPlayerHistoryLine(e.getPlayer(), this.core.getEconomyManager(), String.format("&c&l-%,d TOKENS &f(%s).&7Current Tokens: &e%,d", e.getAmount(), e.getCause().name(), this.core.getTokens().getApi().getPlayerTokens(e.getPlayer())));
                }).bindWith(this.core);
        Events.subscribe(PlayerRankUpEvent.class, EventPriority.MONITOR)
                .filter(EventFilters.ignoreCancelled())
                .handler(e -> {
                    this.historyManager.createPlayerHistoryLine(e.getPlayer(), this.core.getRanks(), String.format("Rank Up: %s&r -> %s", e.getOldRank().getPrefix(), e.getNewRank().getPrefix()));
                }).bindWith(this.core);
        Events.subscribe(PlayerPrestigeEvent.class, EventPriority.MONITOR)
                .filter(EventFilters.ignoreCancelled())
                .handler(e -> {
                    this.historyManager.createPlayerHistoryLine(e.getPlayer(), this.core.getPrestiges(), String.format("Prestige Up:  %s&r -> %s", e.getOldPrestige().getPrefix(), e.getNewPrestige().getPrefix()));
                }).bindWith(this.core);
        Events.subscribe(GangLeaveEvent.class, EventPriority.MONITOR)
                .filter(EventFilters.ignoreCancelled())
                .handler(e -> {
                    this.historyManager.createPlayerHistoryLine(e.getPlayer(), this.core.getGangs(), String.format("Left Gang: &e%s", e.getGang().getName()));
                }).bindWith(this.core);
        Events.subscribe(GangJoinEvent.class, EventPriority.MONITOR)
                .filter(EventFilters.ignoreCancelled())
                .handler(e -> {
                    this.historyManager.createPlayerHistoryLine(e.getPlayer(), this.core.getGangs(), String.format("Joined Gang: &e%s", e.getGang().getName()));
                }).bindWith(this.core);
        Events.subscribe(GangCreateEvent.class, EventPriority.MONITOR)
                .filter(EventFilters.ignoreCancelled())
                .handler(e -> {
                    if (e.getCreator() instanceof Player) {
                        this.historyManager.createPlayerHistoryLine((OfflinePlayer) e.getCreator(), this.core.getGangs(), String.format("Created Gang: &e%s", e.getGang().getName()));
                    }
                }).bindWith(this.core);
        Events.subscribe(GangDisbandEvent.class, EventPriority.MONITOR)
                .filter(EventFilters.ignoreCancelled())
                .handler(e -> {
                    this.historyManager.createPlayerHistoryLine(e.getGang().getOwnerOffline(), this.core.getGangs(), String.format("Disbanded Gang: &e%s", e.getGang().getName()));
                }).bindWith(this.core);
        Events.subscribe(PlayerMultiplierReceiveEvent.class, EventPriority.MONITOR)
                .handler(e -> {
                    this.historyManager.createPlayerHistoryLine(e.getPlayer(), this.core.getMultipliers(), String.format("Received x%,.2f %s Multiplier for %,d %s", e.getMultiplier(), e.getType(), e.getDuration(), e.getTimeUnit().name()));
                }).bindWith(this.core);
        Events.subscribe(PlayerAutomineEvent.class, EventPriority.MONITOR)
                .filter(EventFilters.ignoreCancelled())
                .handler(e -> {
                    this.historyManager.createPlayerHistoryLine(e.getPlayer(), this.core.getAutoMiner(), String.format("Player is Auto-Mining. Time left: %s", TimeUtil.getTime(e.getTimeLeft() - 1)));
                }).bindWith(this.core);
        Events.subscribe(PlayerAutoMinerTimeModifyEvent.class, EventPriority.MONITOR)
                .handler(e -> {
                    this.historyManager.createPlayerHistoryLine(e.getPlayer(), this.core.getAutoMiner(), String.format("Received %,d %s of Auto-Miner time.", e.getDuration(), e.getTimeUnit().name()));
                }).bindWith(this.core);
    }

    private void registerCommands() {
        Commands.create()
                .assertPermission("supremeprison.history")
                .assertPlayer()
                .handler(c -> {
                    if (c.args().size() != 1) {
                        PlayerUtils.sendMessage(c.sender(), "&c/history <player>");
                        return;
                    }
                    OfflinePlayer target = c.arg(0).parseOrFail(OfflinePlayer.class);
                    new PlayerHistoryGUI(c.sender(), target, this).open();
                }).registerAndBind(this.core, "history");
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public void reload() {
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
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
        this.historyRepository.clearTableData();
    }
}
