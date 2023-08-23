package net.primegames.supremeprison.economy.event;

import net.primegames.supremeprison.api.events.SupremePrisonEvent;
import net.primegames.supremeprison.economy.PrisonCurrency;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerBalanceRemoveEvent extends SupremePrisonEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final PrisonCurrency currency;
    private final OfflinePlayer who;
    @Setter
    private double amount;

    public PlayerBalanceRemoveEvent(OfflinePlayer who, PrisonCurrency currency, double amount) {
        this.currency = currency;
        this.amount = amount;
        this.who = who;
    }

    public static HandlerList getHandlersList() {
        return HANDLERS_LIST;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
