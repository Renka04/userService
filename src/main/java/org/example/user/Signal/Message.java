package org.example.user.Signal;

import org.example.user.Entity.UserKeys;
import org.example.user.Repo.UserKeysRepository;
import org.springframework.stereotype.Service;
import org.whispersystems.libsignal.*;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECPrivateKey;
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.protocol.PreKeySignalMessage;
import org.whispersystems.libsignal.state.PreKeyBundle;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.impl.InMemoryIdentityKeyStore;
import org.whispersystems.libsignal.state.impl.InMemoryPreKeyStore;
import org.whispersystems.libsignal.state.impl.InMemorySessionStore;
import org.whispersystems.libsignal.state.impl.InMemorySignedPreKeyStore;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class Message {
    private final UserKeysRepository userKeysRepository;

    public Message(UserKeysRepository userKeysRepository) {
        this.userKeysRepository = userKeysRepository;
    }

    public String encryptMessage(Long senderId, Long receiverId, String plainText) throws Exception {
        // 1. Get receiver’s keys from DB
        UserKeys receiverKeys = userKeysRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver keys not found"));

        byte[] publicIdentityKeyBytes = Base64.getDecoder().decode(receiverKeys.getIdentityKey());
        byte[] privateIdentityKeyBytes = Base64.getDecoder().decode(receiverKeys.getPrivateIdentityKey());

        IdentityKey publicIdentityKey = new IdentityKey(publicIdentityKeyBytes, 0);
        ECPrivateKey privateIdentityKey = Curve.decodePrivatePoint(privateIdentityKeyBytes);

        IdentityKeyPair identityKeyPair = new IdentityKeyPair(publicIdentityKey, privateIdentityKey);

        byte[] signedPreKeyBytes = Base64.getDecoder().decode(receiverKeys.getSignedPreKey());
        byte[] oneTimePreKeyBytes = Base64.getDecoder().decode(receiverKeys.getOneTimePreKeys().get(0)); // take one

        IdentityKey identityKey = new IdentityKey(publicIdentityKeyBytes, 0);
        SignedPreKeyRecord signedPreKey = new SignedPreKeyRecord(signedPreKeyBytes);
        PreKeyRecord oneTimePreKey = new PreKeyRecord(oneTimePreKeyBytes);

        PreKeyBundle preKeyBundle = new PreKeyBundle(
                1, // registrationId (can be any int here if you don’t have real registrationId)
                1, // deviceId
                oneTimePreKey.getId(),
                oneTimePreKey.getKeyPair().getPublicKey(),
                signedPreKey.getId(),
                signedPreKey.getKeyPair().getPublicKey(),
                signedPreKey.getSignature(),
                identityKey
        );

        SignalProtocolAddress receiverAddress = new SignalProtocolAddress("user_" + receiverId, 1); // deviceId = 1

        InMemorySessionStore sessionStore = new InMemorySessionStore();
        InMemoryPreKeyStore preKeyStore = new InMemoryPreKeyStore();
        InMemorySignedPreKeyStore signedPreKeyStore = new InMemorySignedPreKeyStore();

        // <-- Pass IdentityKeyPair here, not just IdentityKey
        InMemoryIdentityKeyStore identityKeyStore = new InMemoryIdentityKeyStore(identityKeyPair, 1); // registrationId

        SessionBuilder sessionBuilder = new SessionBuilder(sessionStore, preKeyStore, signedPreKeyStore, identityKeyStore, receiverAddress);
        sessionBuilder.process(preKeyBundle); // build session

        SessionCipher cipher = new SessionCipher(sessionStore, preKeyStore, signedPreKeyStore, identityKeyStore, receiverAddress);
        CiphertextMessage encrypted = cipher.encrypt(plainText.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(encrypted.serialize());
    }

    public String decryptMessage(Long receiverId, Long senderId, String encryptedBase64) throws Exception {
        // 1. Fetch receiver's stored keys
        UserKeys receiverKeys = userKeysRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver keys not found"));

        byte[] publicIdentityKeyBytes = Base64.getDecoder().decode(receiverKeys.getIdentityKey());
        byte[] privateIdentityKeyBytes = Base64.getDecoder().decode(receiverKeys.getPrivateIdentityKey());

        IdentityKey publicIdentityKey = new IdentityKey(publicIdentityKeyBytes, 0);
        ECPrivateKey privateIdentityKey = Curve.decodePrivatePoint(privateIdentityKeyBytes);
        IdentityKeyPair identityKeyPair = new IdentityKeyPair(publicIdentityKey, privateIdentityKey);

        InMemorySessionStore sessionStore = new InMemorySessionStore();
        InMemoryPreKeyStore preKeyStore = new InMemoryPreKeyStore();
        InMemorySignedPreKeyStore signedPreKeyStore = new InMemorySignedPreKeyStore();
        InMemoryIdentityKeyStore identityKeyStore = new InMemoryIdentityKeyStore(identityKeyPair, 1); // registrationId

        for (String oneTimePreKeyBase64 : receiverKeys.getOneTimePreKeys()) {
            byte[] oneTimePreKeyBytes = Base64.getDecoder().decode(oneTimePreKeyBase64);
            PreKeyRecord preKeyRecord = new PreKeyRecord(oneTimePreKeyBytes);
            preKeyStore.storePreKey(preKeyRecord.getId(), preKeyRecord);
        }

        byte[] signedPreKeyBytes = Base64.getDecoder().decode(receiverKeys.getSignedPreKey());
        SignedPreKeyRecord signedPreKeyRecord = new SignedPreKeyRecord(signedPreKeyBytes);
        signedPreKeyStore.storeSignedPreKey(signedPreKeyRecord.getId(), signedPreKeyRecord);

        SignalProtocolAddress senderAddress = new SignalProtocolAddress("user_" + senderId, 1); // deviceId = 1

        SessionCipher cipher = new SessionCipher(sessionStore, preKeyStore, signedPreKeyStore, identityKeyStore, senderAddress);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64);
        PreKeySignalMessage message = new PreKeySignalMessage(encryptedBytes);

        byte[] decrypted = cipher.decrypt(message);
        return new String(decrypted, StandardCharsets.UTF_8);
    }



}
