package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final EncryptionService encryptionService;

    public UserService(UserMapper userMapper, EncryptionService encryptionService) {
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    public boolean isUsernameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }

    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        System.setProperty("ENCRYPTION_KEY", encodedKey);
        String encryptedPassword = encryptionService.encryptValue(user.getPassword(), encodedKey);
        return userMapper.insert(new User(null, user.getUsername(), encryptedPassword, encodedKey, user.getFirstName(), user.getLastName()));
    }

    public User getUser(String username) {
        return userMapper.getUser(username);
    }
}
