package net.primegames.supremeprison.multipliers.service.impl;

import net.primegames.supremeprison.multipliers.multiplier.PlayerMultiplier;
import net.primegames.supremeprison.multipliers.repo.MultipliersRepository;
import net.primegames.supremeprison.multipliers.service.MultipliersService;
import org.bukkit.entity.Player;

public class MultipliersServiceImpl implements MultipliersService {

    private final MultipliersRepository repository;

    public MultipliersServiceImpl(MultipliersRepository repository) {
        this.repository = repository;
    }

    @Override
    public void setSellMultiplier(Player player, PlayerMultiplier multiplier) {
        this.repository.saveSellMultiplier(player, multiplier);
    }

    @Override
    public void deleteSellMultiplier(Player player) {
        this.repository.deleteSellMultiplier(player);
    }

    @Override
    public void setTokenMultiplier(Player player, PlayerMultiplier multiplier) {
        this.repository.saveTokenMultiplier(player, multiplier);
    }

    @Override
    public void deleteTokenMultiplier(Player player) {
        this.repository.deleteTokenMultiplier(player);
    }

    @Override
    public PlayerMultiplier getSellMultiplier(Player player) {
        return this.repository.getSellMultiplier(player);
    }

    @Override
    public PlayerMultiplier getTokenMultiplier(Player player) {
        return this.repository.getTokenMultiplier(player);
    }

    @Override
    public void removeExpiredMultipliers() {
        this.repository.removeExpiredMultipliers();
    }
}
