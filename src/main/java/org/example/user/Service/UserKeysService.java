package org.example.user.Service;


import lombok.RequiredArgsConstructor;
import org.example.user.Dto.UserKeysDto;
import org.example.user.Entity.User;
import org.example.user.Entity.UserKeys;
import org.example.user.Repo.UserKeysRepository;
import org.example.user.Repo.UserRepository;
import org.example.user.Signal.SignalKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

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

    public void generateAndSaveKeys(Long userId) throws InvalidKeyException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userKeysRepository.existsById(userId)) {
            throw new RuntimeException("Keys already exist for this user");
        }

        IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
        SignedPreKeyRecord signedPreKey = KeyHelper.generateSignedPreKey(identityKeyPair, 1);
        List<PreKeyRecord> oneTimePreKeys = KeyHelper.generatePreKeys(1000, 10);

        UserKeys keys = new UserKeys();
        keys.setUser(user);  // `@MapsId` uses this to set `userId` automatically
        keys.setIdentityKey(Base64.getEncoder().encodeToString(identityKeyPair.getPublicKey().serialize()));
        keys.setPrivateIdentityKey(Base64.getEncoder().encodeToString(identityKeyPair.getPrivateKey().serialize()));
        keys.setSignedPreKey(Base64.getEncoder().encodeToString(signedPreKey.serialize()));
        keys.setOneTimePreKeys(oneTimePreKeys.stream()
                .map(preKey -> Base64.getEncoder().encodeToString(preKey.serialize()))
                .collect(Collectors.toList()));
        keys.setLastKeyRotation(LocalDateTime.now());

        userKeysRepository.save(keys);
    }


}



