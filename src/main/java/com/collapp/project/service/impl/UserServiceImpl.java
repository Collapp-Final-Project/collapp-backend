package com.collapp.project.service.impl;

import com.collapp.project.dto.user.UserResponse;
import com.collapp.project.entity.User;
import com.collapp.project.exception.ResourceNotFoundException;
import com.collapp.project.mapper.UserMapper;
import com.collapp.project.repository.UserRepository;
import com.collapp.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }
}