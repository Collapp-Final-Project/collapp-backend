package com.collapp.project.mapper;

import com.collapp.project.dto.user.UserResponse;
import com.collapp.project.entity.User;
import com.collapp.project.util.TestObjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperTest {

    private UserMapper userMapper;
    private User user;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        user = TestObjectFactory.createUser();
    }

    @Test
    void toResponse_mapsAllFields() {
        UserResponse response = userMapper.toResponse(user);

        assertNotNull(response);
        assertEquals(user.getId(), response.id());
        assertEquals(user.getFullName(), response.fullName());
        assertEquals(user.getUsername(), response.username());
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getSpecialty(), response.specialty());
        assertEquals(user.getBio(), response.bio());
        assertEquals(user.getAvatarUrl(), response.avatarUrl());
        assertEquals(user.getPortfolioUrl(), response.portfolioUrl());
        assertEquals(user.getInstagramUrl(), response.instagramUrl());
    }
}
