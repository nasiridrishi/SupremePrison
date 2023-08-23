package net.primegames.supremeprison.utils.economy;

import net.primegames.supremeprison.SupremePrison;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class EconomyUtils {

    private static final Economy ECONOMY = SupremePrison.getInstance().getEconomy();

    public static EconomyResponse deposit(Player player, double amount) {
        return ECONOMY.depositPlayer(player, amount);
    }

    public static EconomyResponse withdraw(Player player, double amount) {
        return ECONOMY.withdrawPlayer(player, amount);
    }
}
