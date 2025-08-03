package org.example.user.Dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String bio;
    private String location;
    private String job;
    private String link;
    private String qrCodeUrl;
    private String profileImageUrl;

    public UserDto(Long id, String username, String email, String bio, String location, String job, String link, String qrCodeUrl, String profileImageUrl) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.location = location;
        this.job = job;
        this.link = link;
        this.qrCodeUrl = qrCodeUrl;
        this.profileImageUrl = profileImageUrl;
    }

    public UserDto( ) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
