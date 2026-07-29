package com.collapp.project.service.impl;

import com.collapp.project.dto.user.UserResponse;
import com.collapp.project.dto.user.UserUpdateRequest;
import com.collapp.project.entity.User;
import com.collapp.project.exception.ResourceNotFoundException;
import com.collapp.project.mapper.UserMapper;
import com.collapp.project.repository.UserRepository;
import com.collapp.project.util.TestObjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = TestObjectFactory.createUser();
        userResponse = new UserResponse(
                user.getId(), user.getFullName(), user.getUsername(), user.getEmail(),
                user.getSpecialty(), user.getBio(), user.getAvatarUrl(),
                user.getPortfolioUrl(), user.getInstagramUrl()
        );
    }

    @Test
    void getCurrentUser_success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getCurrentUser(user.getEmail());

        assertNotNull(result);
        assertEquals(user.getEmail(), result.email());
        assertEquals(user.getUsername(), result.username());
        verify(userRepository).findByEmail(user.getEmail());
        verify(userMapper).toResponse(user);
    }

    @Test
    void getCurrentUser_notFound_throws() {
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getCurrentUser("nonexistent@test.com"));
    }

    @Test
    void updateCurrentUser_success() {
        UserUpdateRequest updateRequest = TestObjectFactory.createUserUpdateRequest();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.updateCurrentUser(user.getEmail(), updateRequest);

        assertNotNull(result);
        verify(userRepository).save(user);
        verify(userMapper).toResponse(user);
    }

    @Test
    void updateCurrentUser_partialUpdate() {
        UserUpdateRequest partialUpdate = new UserUpdateRequest(null, "Only bio", null, null, null);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.updateCurrentUser(user.getEmail(), partialUpdate);

        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void updateCurrentUser_notFound_throws() {
        UserUpdateRequest updateRequest = TestObjectFactory.createUserUpdateRequest();

        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateCurrentUser("nonexistent@test.com", updateRequest));

        verify(userRepository, never()).save(any());
    }
}
