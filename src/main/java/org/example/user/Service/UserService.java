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
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
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

        String qrText = "https://yourfrontend.com/user/" + savedUser.getId();

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 250, 250);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            String base64Qr = Base64.getEncoder().encodeToString(pngData);

            UserDto resultDto = userMapper.toDto(savedUser);
            resultDto.setQrCodeUrl("data:image/png;base64," + base64Qr);

            return resultDto;

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return userMapper.toDto(savedUser);
        }
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
            existingUser.setMobilePhone(dto.getMobilePhone());
            existingUser.setGender(dto.getGender());
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

    public String saveProfileImage(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        String fileName = "profile_" + userId + "_" + System.currentTimeMillis() + ".png";

        Path uploadPath = Paths.get("uploads/images/");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String imageUrl = "https://userservice-mm2v.onrender.com/uploads/images/" + fileName;

        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }

}
