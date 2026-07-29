package com.collapp.project.seeder;

import com.collapp.project.entity.User;
import com.collapp.project.entity.enums.Specialty;
import com.collapp.project.entity.enums.SystemRole;
import com.collapp.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.count() > 0) {
            return; 
        }

        User admin = User.builder()
                .username("admin")
                .fullName("Administrador Collapp")
                .email("admin@collapp.com")
                .passwordHash(passwordEncoder.encode("12345678"))
                .systemRole(SystemRole.ROLE_ADMIN)
                .specialty(Specialty.PRODUCTION)
                .build();

        User maquilladora = User.builder()
                .username("lola_sfx")
                .fullName("Lola Fernández")
                .email("lola@makeup.com")
                .passwordHash(passwordEncoder.encode("12345678"))
                .systemRole(SystemRole.ROLE_CREATIVE)
                .specialty(Specialty.MAKEUP)
                .bio("Especialista en maquillaje SFX para cine y series.")
                .build();

        User fotografo = User.builder()
                .username("alex_photo")
                .fullName("Alex Rivera")
                .email("alex@photo.com")
                .passwordHash(passwordEncoder.encode("12345678"))
                .systemRole(SystemRole.ROLE_CREATIVE)
                .specialty(Specialty.PHOTOGRAPHY_VIDEO)
                .portfolioUrl("https://alexrivera-portfolio.com")
                .build();

        userRepository.save(admin);
        userRepository.save(maquilladora);
        userRepository.save(fotografo);

        log.info("DataSeeder: usuarios de prueba creados (admin / lola_sfx / alex_photo)");
    }
}