package org.example.user.Dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String bio;

    public UserDto(Long id, String username, String email, String bio) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.bio = bio;
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
}
