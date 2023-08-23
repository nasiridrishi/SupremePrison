package net.primegames.supremeprison.tokens.task;

import net.primegames.supremeprison.tokens.SupremePrisonTokens;
import me.lucko.helper.Schedulers;
import me.lucko.helper.scheduler.Task;
import me.lucko.helper.utils.Players;

import java.util.concurrent.TimeUnit;


public final class SavePlayerDataTask implements Runnable {

    private final SupremePrisonTokens plugin;
    private Task task;

    public SavePlayerDataTask(SupremePrisonTokens plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.plugin.getTokensManager().savePlayerData(Players.all(), false, true);
    }

    public void start() {
        stop();
        this.task = Schedulers.async().runRepeating(this, 30, TimeUnit.SECONDS, this.plugin.getTokensConfig().getSavePlayerDataInterval(), TimeUnit.MINUTES);
    }

    public void stop() {
        if (task != null) {
            task.stop();
        }
    }
}
