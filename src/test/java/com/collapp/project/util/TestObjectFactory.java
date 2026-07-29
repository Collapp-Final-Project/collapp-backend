package com.collapp.project.util;

import com.collapp.project.dto.application.ApplicationRequest;
import com.collapp.project.dto.application.ApplicationStatusRequest;
import com.collapp.project.dto.auth.LoginRequest;
import com.collapp.project.dto.auth.RegisterRequest;
import com.collapp.project.dto.offer.OfferRequest;
import com.collapp.project.dto.user.UserUpdateRequest;
import com.collapp.project.entity.Application;
import com.collapp.project.entity.Offer;
import com.collapp.project.entity.User;
import com.collapp.project.entity.enums.ApplicationStatus;
import com.collapp.project.entity.enums.CompensationType;
import com.collapp.project.entity.enums.OfferStatus;
import com.collapp.project.entity.enums.Specialty;
import com.collapp.project.entity.enums.SystemRole;

import java.time.LocalDate;

public class TestObjectFactory {

    public static final Long USER_ID = 1L;
    public static final Long USER2_ID = 2L;
    public static final String USER_EMAIL = "user@test.com";
    public static final String USER2_EMAIL = "user2@test.com";
    public static final String USER_USERNAME = "testuser";
    public static final String USER2_USERNAME = "testuser2";
    public static final String USER_PASSWORD = "password123";
    public static final String USER_FULL_NAME = "Test User";
    public static final Long OFFER_ID = 1L;
    public static final Long APP_ID = 1L;

    public static User createUser() {
        return User.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .username(USER_USERNAME)
                .fullName(USER_FULL_NAME)
                .passwordHash("$2a$encodedpasswordhashxxxxxxxxxxxxx")
                .systemRole(SystemRole.ROLE_CREATIVE)
                .specialty(Specialty.MAKEUP)
                .bio("A test bio")
                .avatarUrl("https://avatar.url/test.png")
                .portfolioUrl("https://portfolio.url")
                .instagramUrl("https://instagram.com/test")
                .build();
    }

    public static User createUser2() {
        return User.builder()
                .id(USER2_ID)
                .email(USER2_EMAIL)
                .username(USER2_USERNAME)
                .fullName("Test User 2")
                .passwordHash("$2a$encodedpasswordhashxxxxxxxxxxxxx")
                .systemRole(SystemRole.ROLE_CREATIVE)
                .specialty(Specialty.PHOTOGRAPHY_VIDEO)
                .bio("Another bio")
                .avatarUrl("https://avatar.url/test2.png")
                .build();
    }

    public static User createAdminUser() {
        return User.builder()
                .id(99L)
                .email("admin@test.com")
                .username("admin")
                .fullName("Admin User")
                .passwordHash("$2a$encodedpasswordhashxxxxxxxxxxxxx")
                .systemRole(SystemRole.ROLE_ADMIN)
                .specialty(Specialty.PRODUCTION)
                .build();
    }

    public static Offer createOffer(User creator) {
        return Offer.builder()
                .id(OFFER_ID)
                .creator(creator)
                .title("Test Offer")
                .description("A test offer description")
                .category(Specialty.MAKEUP)
                .location("Barcelona")
                .startDate(LocalDate.of(2026, 8, 1))
                .endDate(LocalDate.of(2026, 8, 15))
                .compensationType(CompensationType.PAID)
                .status(OfferStatus.OPEN)
                .build();
    }

    public static Offer createOffer(Long id, User creator, String title) {
        return Offer.builder()
                .id(id)
                .creator(creator)
                .title(title)
                .description("Description for " + title)
                .category(Specialty.MAKEUP)
                .location("Barcelona")
                .startDate(LocalDate.of(2026, 8, 1))
                .endDate(LocalDate.of(2026, 8, 15))
                .compensationType(CompensationType.PAID)
                .status(OfferStatus.OPEN)
                .build();
    }

    public static Application createApplication(Offer offer, User applicant) {
        return Application.builder()
                .id(APP_ID)
                .offer(offer)
                .applicant(applicant)
                .message("I am interested in this offer")
                .status(ApplicationStatus.PENDING)
                .build();
    }

    public static Application createApplication(Long id, Offer offer, User applicant, ApplicationStatus status) {
        return Application.builder()
                .id(id)
                .offer(offer)
                .applicant(applicant)
                .message("Application message")
                .status(status)
                .build();
    }

    public static RegisterRequest createRegisterRequest() {
        return new RegisterRequest(
                "newuser",
                "New User",
                "new@test.com",
                "password123",
                Specialty.MAKEUP,
                null, null, null, null
        );
    }

    public static LoginRequest createLoginRequest() {
        return new LoginRequest(USER_EMAIL, USER_PASSWORD);
    }

    public static OfferRequest createOfferRequest() {
        return new OfferRequest(
                "Test Offer",
                "A test offer description",
                Specialty.MAKEUP,
                "Barcelona",
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 15),
                CompensationType.PAID,
                OfferStatus.OPEN
        );
    }

    public static OfferRequest createOfferRequest(String title) {
        return new OfferRequest(
                title,
                "Description for " + title,
                Specialty.PHOTOGRAPHY_VIDEO,
                "Madrid",
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 15),
                CompensationType.COLLABORATION,
                OfferStatus.OPEN
        );
    }

    public static ApplicationRequest createApplicationRequest() {
        return new ApplicationRequest(OFFER_ID, "I am interested!");
    }

    public static ApplicationStatusRequest createApplicationStatusRequest(ApplicationStatus status) {
        return new ApplicationStatusRequest(status);
    }

    public static UserUpdateRequest createUserUpdateRequest() {
        return new UserUpdateRequest(
                "Updated Name",
                "Updated bio",
                "https://new.avatar.url",
                "https://new.portfolio.url",
                "https://new.instagram.url"
        );
    }
}
