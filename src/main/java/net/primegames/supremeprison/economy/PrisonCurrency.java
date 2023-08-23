package net.primegames.supremeprison.economy;

import lombok.Getter;
import me.TechsCode.UltraEconomy.UltraEconomy;
import me.TechsCode.UltraEconomy.objects.Currency;
import org.jetbrains.annotations.NotNull;

@Getter
public enum PrisonCurrency {

    TOKEN("Token"),
    GEM("Gems");

    private final String name;

    PrisonCurrency(String name) {
        this.name = name;
    }

    public @NotNull Currency get() {
        return UltraEconomy.getAPI().getCurrencies().name(this.name).get();
    }

}
