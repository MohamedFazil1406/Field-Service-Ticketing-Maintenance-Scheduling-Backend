package com.nova.fieldops.user;

import com.nova.fieldops.user.dto.CreateUserRequest;
import com.nova.fieldops.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException(
                    "User with this email already exists"
            );
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .passwordHash(
                        passwordEncoder.encode(request.password())
                )
                .role(request.role())
                .createdAt(LocalDateTime.now())   // FIX
                .build();

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }
}