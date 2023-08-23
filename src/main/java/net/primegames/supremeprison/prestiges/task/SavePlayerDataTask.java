package net.primegames.supremeprison.prestiges.task;

import net.primegames.supremeprison.prestiges.SupremePrisonPrestiges;
import me.lucko.helper.Schedulers;
import me.lucko.helper.scheduler.Task;
import me.lucko.helper.utils.Players;

import java.util.concurrent.TimeUnit;


public final class SavePlayerDataTask implements Runnable {

    private final SupremePrisonPrestiges plugin;
    private Task task;

    public SavePlayerDataTask(SupremePrisonPrestiges plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Players.all().forEach(p -> this.plugin.getPrestigeManager().savePlayerData(p, false, true));
    }

    public void start() {
        this.stop();
        this.task = Schedulers.async().runRepeating(this, 30, TimeUnit.SECONDS, this.plugin.getPrestigeConfig().getSavePlayerDataInterval(), TimeUnit.MINUTES);
    }

    public void stop() {
        if (task != null) {
            task.stop();
        }
    }
}
