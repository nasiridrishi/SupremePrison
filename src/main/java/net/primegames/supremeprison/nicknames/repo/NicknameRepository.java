package net.primegames.supremeprison.nicknames.repo;

import net.primegames.supremeprison.interfaces.UPCRepository;
import org.bukkit.OfflinePlayer;

public interface NicknameRepository extends UPCRepository {

    void updatePlayerNickname(OfflinePlayer player);

}
