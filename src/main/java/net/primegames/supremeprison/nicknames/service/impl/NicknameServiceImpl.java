package net.primegames.supremeprison.nicknames.service.impl;

import net.primegames.supremeprison.nicknames.repo.NicknameRepository;
import net.primegames.supremeprison.nicknames.service.NicknameService;
import org.bukkit.OfflinePlayer;

public class NicknameServiceImpl implements NicknameService {

    private final NicknameRepository repository;

    public NicknameServiceImpl(NicknameRepository repository) {

        this.repository = repository;
    }

    @Override
    public void updatePlayerNickname(OfflinePlayer player) {
        repository.updatePlayerNickname(player);
    }
}
