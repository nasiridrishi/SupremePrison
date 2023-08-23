package net.primegames.supremeprison.gangs.model;

import net.primegames.supremeprison.gangs.SupremePrisonGangs;
import me.lucko.helper.Schedulers;
import me.lucko.helper.scheduler.Task;

import java.util.concurrent.TimeUnit;

public final class GangUpdateTopTask implements Runnable {

    private final SupremePrisonGangs plugin;
    private final GangTopProvider gangTopProvider;

    private Task task;

    public GangUpdateTopTask(SupremePrisonGangs plugin, GangTopProvider gangTopProvider) {
        this.plugin = plugin;
        this.gangTopProvider = gangTopProvider;
    }

    @Override
    public void run() {
        this.plugin.getGangsManager().updateGangTop(gangTopProvider);
    }

    public void start() {
        this.stop();
        int delay = this.plugin.getConfig().getGangUpdateDelay();
        this.task = Schedulers.async().runRepeating(this, delay, TimeUnit.MINUTES, delay, TimeUnit.MINUTES);
    }

    public void stop() {
        if (this.task != null) {
            this.task.stop();
        }
    }
}
