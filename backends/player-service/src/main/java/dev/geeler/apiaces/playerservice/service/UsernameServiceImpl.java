package dev.geeler.apiaces.playerservice.service;

import dev.geeler.apiaces.playerservice.repository.UsernameAdjRepository;
import dev.geeler.apiaces.playerservice.repository.UsernameNomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsernameServiceImpl implements UsernameService {
    @Autowired
    private UsernameAdjRepository usernameAdjRepository;
    @Autowired
    private UsernameNomRepository usernameNomRepository;

    @Override
    public String getRandomUsername() {
        return usernameAdjRepository.getRandomAdj().getValue() + usernameNomRepository.getRandomNom().getValue();
    }
}
