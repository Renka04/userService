package org.example.user.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class UserKeysDto {
    private String identityKey;
    private String signedPreKey;
    private List<String> oneTimePreKeys;

    public UserKeysDto(String identityKey, String signedPreKey, List<String> oneTimePreKeys) {
        this.identityKey = identityKey;
        this.signedPreKey = signedPreKey;
        this.oneTimePreKeys = oneTimePreKeys;
    }

    public UserKeysDto( ){
    }

    public String getIdentityKey() {
        return identityKey;
    }

    public void setIdentityKey(String identityKey) {
        this.identityKey = identityKey;
    }

    public String getSignedPreKey() {
        return signedPreKey;
    }

    public void setSignedPreKey(String signedPreKey) {
        this.signedPreKey = signedPreKey;
    }

    public List<String> getOneTimePreKeys() {
        return oneTimePreKeys;
    }

    public void setOneTimePreKeys(List<String> oneTimePreKeys) {
        this.oneTimePreKeys = oneTimePreKeys;
    }
}

