package dev.drawethree.xprison.economy;

import dev.drawethree.xprison.XPrison;
import dev.drawethree.xprison.XPrisonModule;
import dev.drawethree.xprison.economy.event.PlayerBalanceReceiveEvent;
import dev.drawethree.xprison.economy.event.PlayerBalanceRemoveEvent;
import lombok.Getter;
import me.TechsCode.UltraEconomy.UltraEconomy;
import me.TechsCode.UltraEconomy.UltraEconomyAPI;
import me.TechsCode.UltraEconomy.item.CurrencyItemFactory;
import me.TechsCode.UltraEconomy.objects.Account;
import me.TechsCode.UltraEconomy.objects.Currency;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;


@Getter
@Getter
public class PrisonEconomyManager implements XPrisonModule {

    public static PrisonEconomyManager instance;

    public PrisonEconomyManager() {
        instance = this;
    }


    public void addBalance(PrisonCurrency currency, Player player, double amount) {
        Account account = getAccount(player);
        if (account == null) return;
        PlayerBalanceReceiveEvent event = new PlayerBalanceReceiveEvent(player, currency, amount);
        Bukkit.getPluginManager().callEvent(event);
        account.addBalance(currency.get(), event.getAmount());
        XPrison.getInstance().getLogger().log(Level.INFO, "Added " + event.getAmount() + " " + currency.getName() + " to " + player.getName());
    }

    public void takeBalance(PrisonCurrency currency, OfflinePlayer player, double amount) {
        Account account = getAccount(player);
        if (account == null) return;
        PlayerBalanceRemoveEvent event = new PlayerBalanceRemoveEvent(player, currency, amount);
        Bukkit.getPluginManager().callEvent(event);
        account.removeBalance(currency.get(), event.getAmount());
        XPrison.getInstance().getLogger().log(Level.INFO, "Took " + event.getAmount() + " " + currency.getName() + " from " + player.getName());
    }

    public void removeBalance(PrisonCurrency currencies, OfflinePlayer player, double amount) {
        takeBalance(currencies, player, amount);
    }

    public double getBalance(PrisonCurrency currency, OfflinePlayer player) {
        Account account = getAccount(player);
        if (account == null) return 0;
        return account.getBalance(currency.get()).getOnHand();
    }

    public boolean hasBalance(PrisonCurrency currency, Player player, double amount) {
        Account account = getAccount(player);
        if (account == null) return false;
        return account.getBalance(currency.get()).getOnHand() >= amount;
    }

    private Account getAccount(OfflinePlayer player) {
        Optional<Account> optAccount = UltraEconomy.getAPI().getAccounts().name(player.getName());
        if (!optAccount.isPresent()) {
            XPrison.getInstance().getLogger().log(Level.SEVERE, "Account not found for player " + player.getName(), new IllegalStateException("Account not found!"));
            return null;
        }
        return optAccount.get();
    }

    public void dropBalanceAsItem(World world, Location location, PrisonCurrency currencies, double amount) {
        UltraEconomyAPI api = UltraEconomy.getAPI();
        Currency currency = api.getCurrencies().name(currencies.getName()).get();
        world.dropItemNaturally(location, CurrencyItemFactory.newItem(currency, amount));
    }

    public void setBalance(PrisonCurrency prisonCurrency, OfflinePlayer p, double newAmount) {
        Account account = getAccount(p);
        if (account == null) {
            XPrison.getInstance().getLogger().log(Level.SEVERE, "Account not found for player " + p.getName(), new IllegalStateException("Account not found!"));
            return;
        }
        account.getBalance(prisonCurrency.get()).setHand(newAmount);
    }

    @Override
    public void enable() {
        ArrayList<String> missingCurrencies = new ArrayList<>();
        for (PrisonCurrency currency : PrisonCurrency.values()) {
            Optional<Currency> optC = UltraEconomy.getAPI().getCurrencies().name(currency.getName());
            if (!optC.isPresent()) {
                missingCurrencies.add(currency.getName());
            }
        }
        if (!missingCurrencies.isEmpty()) {
            XPrison.getInstance().getLogger().log(Level.SEVERE, "Missing currencies: " + missingCurrencies);
            ArrayList<String> currencies = new ArrayList<>();
            for (Currency currency : UltraEconomy.getAPI().getCurrencies()) {
                currencies.add(currency.getName());
            }
            XPrison.getInstance().getLogger().log(Level.SEVERE, "Available currencies: " + currencies);
            XPrison.getInstance().getLogger().log(Level.SEVERE, "Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(XPrison.getInstance());
        }
    }

    @Override
    public void disable() {
        //NOOP
    }

    @Override
    public void reload() {
        //NOOP
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "PrisonEconomy";
    }

    @Override
    public boolean isHistoryEnabled() {
        return true;
    }

    @Override
    public void resetPlayerData() {
        UltraEconomy.getInstance().getAccounts().forEach(account -> account.getBalance(PrisonCurrency.TOKEN.get()).setHand(0));
    }
}
