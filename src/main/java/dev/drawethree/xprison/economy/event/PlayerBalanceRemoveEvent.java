package dev.drawethree.xprison.economy.event;

import dev.drawethree.xprison.api.events.XPrisonEvent;
import dev.drawethree.xprison.economy.PrisonCurrency;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerBalanceRemoveEvent extends XPrisonEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final PrisonCurrency currency;
    @Setter
    private double amount;
    private final OfflinePlayer who;

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
