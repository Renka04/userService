package org.example.user.Signal;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class SignalKeyUtils {

    public static IdentityKeyPair generateIdentityKeyPair() {
        return KeyHelper.generateIdentityKeyPair();
    }

    public static SignedPreKeyRecord generateSignedPreKey(IdentityKeyPair identityKeyPair, int id) throws InvalidKeyException {
        return KeyHelper.generateSignedPreKey(identityKeyPair, id);
    }

    public static List<PreKeyRecord> generateOneTimePreKeys(int startId, int count) {
        return KeyHelper.generatePreKeys(startId, count);
    }

    public static String encode(byte[] keyBytes) {
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public static List<String> encodePreKeys(List<PreKeyRecord> preKeys) {
        return preKeys.stream()
                .map(preKey -> encode(preKey.serialize()))
                .collect(Collectors.toList());
    }
}
