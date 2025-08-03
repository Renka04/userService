package org.example.user.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.example.user.Dto.UserDto;
import org.example.user.Entity.User;
import org.example.user.Mapper.UserMapper;
import org.example.user.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);

        User savedUser = userRepository.save(user);

        String qrText = "https://yourfrontend.com/user/" + savedUser.getId(); // or your real link
        String fileName = "user_" + savedUser.getId() + ".png";
        String qrFilePath = "src/main/resources/static/qr/" + fileName;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 250, 250);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", java.nio.file.Paths.get(qrFilePath));

            savedUser.setQrCodeUrl("https://userservice-mm2v.onrender.com/qr/" + fileName);

            savedUser = userRepository.save(savedUser);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        return userMapper.toDto(savedUser);
    }



//    public UserDto createUser(UserDto userDto) {
//        User user = userMapper.toEntity(userDto);
//        User savedUser = userRepository.save(user);
//        return userMapper.toDto(savedUser);
//    }

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
            existingUser.setLocation(dto.getLocation());
            existingUser.setJob(dto.getJob());
            existingUser.setLink(dto.getLink());
            existingUser.setQrCodeUrl(dto.getQrCodeUrl());
            existingUser.setProfileImageUrl(dto.getProfileImageUrl());
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
