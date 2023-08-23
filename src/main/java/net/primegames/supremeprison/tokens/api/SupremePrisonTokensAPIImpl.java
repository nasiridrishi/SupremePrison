package net.primegames.supremeprison.tokens.api;

import net.primegames.supremeprison.api.enums.LostCause;
import net.primegames.supremeprison.api.enums.ReceiveCause;
import net.primegames.supremeprison.economy.PrisonCurrency;
import net.primegames.supremeprison.economy.PrisonEconomyManager;
import net.primegames.supremeprison.tokens.managers.TokensManager;
import org.bukkit.OfflinePlayer;

public final class SupremePrisonTokensAPIImpl implements SupremePrisonTokensAPI {

    private final TokensManager manager;

    public SupremePrisonTokensAPIImpl(TokensManager manager) {
        this.manager = manager;
    }

    @Override
    public long getPlayerTokens(OfflinePlayer p) {
        return this.manager.getPlayerTokens(p);
    }

    @Override
    public boolean hasEnough(OfflinePlayer p, long amount) {
        return this.getPlayerTokens(p) >= amount;
    }

    @Override
    public void removeTokens(OfflinePlayer p, long amount, LostCause cause) {
        PrisonEconomyManager.getInstance().removeBalance(PrisonCurrency.TOKEN, p, amount);
    }

    @Override
    public void addTokens(OfflinePlayer p, long amount, ReceiveCause cause) {
        this.manager.giveTokens(p, amount, null, cause, null);
    }
}
