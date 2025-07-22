package org.example.user.Service;

import org.example.user.Dto.UserDto;
import org.example.user.Entity.ChatMessage;
import org.example.user.Entity.User;
import org.example.user.Mapper.UserMapper;
import org.example.user.Repo.ChatMessageRepository;
import org.example.user.Repo.UserRepository;
import org.example.user.Signal.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final MessageService messageService;

//    @Autowired
//    public UserService(UserRepository userRepository, UserMapper userMapper) {
//        this.userRepository = userRepository;
//        this.userMapper = userMapper;
//    }

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, ChatMessageRepository chatMessageRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.chatMessageRepository=chatMessageRepository;
        this.messageService=messageService;
    }

    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElse(null);
    }

    public UserDto updateUser(Long id, UserDto dto) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setUsername(dto.getUsername());
            existingUser.setEmail(dto.getEmail());
            existingUser.setBio(dto.getBio());
            return userMapper.toDto(userRepository.save(existingUser));
        }).orElse(null);
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
