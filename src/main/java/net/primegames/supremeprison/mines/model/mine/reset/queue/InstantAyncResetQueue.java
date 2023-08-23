package net.primegames.supremeprison.mines.model.mine.reset.queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InstantAyncResetQueue {

    private final ExecutorService executor;
    private final Queue<Runnable> taskQueue;
    private boolean isRunning;

    public InstantAyncResetQueue() {
        executor = Executors.newSingleThreadExecutor();
        taskQueue = new LinkedList<>();
        isRunning = false;
    }

    public synchronized void enqueueTask(Runnable task) {
        taskQueue.add(task);
        if (!isRunning) {
            isRunning = true;
            startNextTask();
        }
    }

    private synchronized void startNextTask() {
        Runnable task = taskQueue.poll();
        if (task != null) {
            CompletableFuture.runAsync(task, executor)
                    .thenRunAsync(this::startNextTask, executor);
        } else {
            isRunning = false;
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
