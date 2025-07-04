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
                user.getBio()
        );
    }

    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        return new User(
                dto.getId(),
                dto.getUsername(),
                dto.getEmail(),
                dto.getBio()
        );
    }
}