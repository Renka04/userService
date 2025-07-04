package org.example.user.Entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_keys")
public class UserKeys {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    private String identityKey;

    @Lob
    private String signedPreKey;

    public UserKeys(Long userId, User user, String identityKey, String signedPreKey, List<String> oneTimePreKeys, LocalDateTime lastKeyRotation) {
        this.userId = userId;
        this.user = user;
        this.identityKey = identityKey;
        this.signedPreKey = signedPreKey;
        this.oneTimePreKeys = oneTimePreKeys;
        this.lastKeyRotation = lastKeyRotation;
    }

    public UserKeys() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public LocalDateTime getLastKeyRotation() {
        return lastKeyRotation;
    }

    public void setLastKeyRotation(LocalDateTime lastKeyRotation) {
        this.lastKeyRotation = lastKeyRotation;
    }

    @ElementCollection
    @CollectionTable(name = "one_time_pre_keys", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "pre_key")


    private List<String> oneTimePreKeys;

    private LocalDateTime lastKeyRotation;
}

