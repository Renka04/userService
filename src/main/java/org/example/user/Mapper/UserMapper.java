package org.example.user.Mapper;

import org.example.user.Entity.User;
import org.example.user.Dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getBio(),
                user.getLocation(),
                user.getJob(),
                user.getLink(),
                user.getQrCodeUrl(),
                user.getProfileImageUrl(),
                user.getMobilePhone(),
                user.getGender()
        );
    }

    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setBio(dto.getBio());
        user.setLocation(dto.getLocation());
        user.setJob(dto.getJob());
        user.setLink(dto.getLink());
        user.setQrCodeUrl(dto.getQrCodeUrl());
        user.setProfileImageUrl(dto.getProfileImageUrl());
        user.setMobilePhone(dto.getMobilePhone());
        user.setGender(dto.getGender());
        return user;
    }
}
