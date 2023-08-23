package net.primegames.supremeprison;

public interface SupremePrisonModule {

    void enable();

    void disable();

    void reload();

    boolean isEnabled();

    String getName();

    boolean isHistoryEnabled();

    void resetPlayerData();
}
