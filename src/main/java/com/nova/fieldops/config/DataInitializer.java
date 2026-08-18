package com.nova.fieldops.config;

import com.nova.fieldops.user.User;
import com.nova.fieldops.user.UserRepository;
import com.nova.fieldops.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.findByEmail("admin@fieldops.com").isEmpty()) {

            User admin = User.builder()
                    .name("FieldOps Admin")
                    .email("admin@fieldops.com")
                    .passwordHash(
                            passwordEncoder.encode("Admin@123")
                    )
                    .role(UserRole.ADMIN)
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(admin);

            System.out.println("Initial admin user created.");
        }
    }
}