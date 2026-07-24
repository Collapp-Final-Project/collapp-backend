package com.collapp.project.seeder;

import com.collapp.project.entity.User;
import com.collapp.project.entity.enums.Specialty;
import com.collapp.project.entity.enums.SystemRole;
import com.collapp.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.count() > 0) {
            return; // ya hay datos, no se vuelve a sembrar
        }

        User admin = User.builder()
                .username("admin")
                .fullName("Administrador Collapp")
                .email("admin@collapp.com")
                .passwordHash(passwordEncoder.encode("admin123"))
                .systemRole(SystemRole.ROLE_ADMIN)
                .specialty(Specialty.PRODUCTION)
                .build();

        User maquilladora = User.builder()
                .username("lola_sfx")
                .fullName("Lola Fernández")
                .email("lola@creativo.com")
                .passwordHash(passwordEncoder.encode("123456"))
                .systemRole(SystemRole.ROLE_CREATIVE)
                .specialty(Specialty.MAKEUP)
                .bio("Especialista en maquillaje SFX para cine y series.")
                .build();

        User fotografo = User.builder()
                .username("alex_photo")
                .fullName("Alex Rivera")
                .email("alex@creativo.com")
                .passwordHash(passwordEncoder.encode("123456"))
                .systemRole(SystemRole.ROLE_CREATIVE)
                .specialty(Specialty.PHOTOGRAPHY_VIDEO)
                .portfolioUrl("https://alexrivera-portfolio.com")
                .build();

        userRepository.save(admin);
        userRepository.save(maquilladora);
        userRepository.save(fotografo);

        System.out.println("DataSeeder: usuarios de prueba creados (admin / lola_sfx / alex_photo)");
    }
}