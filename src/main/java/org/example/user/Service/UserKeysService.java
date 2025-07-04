package org.example.user.Service;


import lombok.RequiredArgsConstructor;
import org.example.user.Dto.UserKeysDto;
import org.example.user.Entity.User;
import org.example.user.Entity.UserKeys;
import org.example.user.Repo.UserKeysRepository;
import org.example.user.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserKeysService {

    private final UserKeysRepository userKeysRepository;
    private final UserRepository userRepository;

    public UserKeysService(UserKeysRepository userKeysRepository, UserRepository userRepository) {
        this.userKeysRepository = userKeysRepository;
        this.userRepository = userRepository;
    }

    public void saveKeys(Long userId, UserKeysDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserKeys keys = new UserKeys();
        keys.setUser(user);
        keys.setIdentityKey(dto.getIdentityKey());
        keys.setSignedPreKey(dto.getSignedPreKey());
        keys.setOneTimePreKeys(dto.getOneTimePreKeys());
        keys.setLastKeyRotation(LocalDateTime.now());

        userKeysRepository.save(keys);
    }

    public UserKeysDto getKeys(Long userId) {
        UserKeys keys = userKeysRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Keys not found"));

        return new UserKeysDto(
                keys.getIdentityKey(),
                keys.getSignedPreKey(),
                keys.getOneTimePreKeys()
        );
    }
}



