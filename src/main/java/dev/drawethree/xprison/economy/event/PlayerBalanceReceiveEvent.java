package dev.drawethree.xprison.economy.event;

import dev.drawethree.xprison.economy.PrisonCurrency;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;


@Getter
public class PlayerBalanceReceiveEvent extends PlayerEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final PrisonCurrency currency;
    @Setter
    private double amount;

    public PlayerBalanceReceiveEvent(@NotNull Player who, PrisonCurrency currency, double amount) {
        super(who);
        this.currency = currency;
        this.amount = amount;
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
