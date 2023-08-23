package net.primegames.supremeprison.gangs.service.impl;

import net.primegames.supremeprison.gangs.model.Gang;
import net.primegames.supremeprison.gangs.model.GangInvitation;
import net.primegames.supremeprison.gangs.repo.GangsRepository;
import net.primegames.supremeprison.gangs.service.GangsService;

import java.util.List;

public class GangsServiceImpl implements GangsService {

    private final GangsRepository repository;

    public GangsServiceImpl(GangsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void updateGang(Gang g) {
        repository.updateGang(g);
    }

    @Override
    public void deleteGang(Gang g) {
        repository.deleteGang(g);
    }

    @Override
    public void createGang(Gang g) {
        repository.createGang(g);
    }

    @Override
    public List<Gang> getAllGangs() {
        return repository.getAllGangs();
    }

    @Override
    public List<GangInvitation> getGangInvitations(Gang gang) {
        return repository.getGangInvitations(gang);
    }

    @Override
    public void createGangInvitation(GangInvitation gangInvitation) {
        repository.createGangInvitation(gangInvitation);
    }

    @Override
    public void deleteGangInvitation(GangInvitation gangInvitation) {
        repository.deleteGangInvitation(gangInvitation);
    }
}
