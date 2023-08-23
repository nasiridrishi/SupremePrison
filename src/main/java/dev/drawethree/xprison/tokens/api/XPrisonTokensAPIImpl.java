package dev.drawethree.xprison.tokens.api;

import dev.drawethree.xprison.api.enums.LostCause;
import dev.drawethree.xprison.api.enums.ReceiveCause;
import dev.drawethree.xprison.economy.PrisonCurrency;
import dev.drawethree.xprison.economy.PrisonEconomyManager;
import dev.drawethree.xprison.tokens.managers.TokensManager;
import org.bukkit.OfflinePlayer;

public final class XPrisonTokensAPIImpl implements XPrisonTokensAPI {

    private final TokensManager manager;

    public XPrisonTokensAPIImpl(TokensManager manager) {
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
