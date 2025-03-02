package com.twitter.Service;

import com.twitter.Entity.Role;
import com.twitter.Entity.User;
import com.twitter.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        throw new RuntimeException("Kimlik doğrulama başarısız!");
    }

    @Override
    public User register(User user) {
        boolean userExists = userRepository.existsByEmail(user.getEmail());
        if (userExists) {
            throw new RuntimeException("E-posta zaten kayıtlı!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Yeni kullanıcıya varsayılan olarak USER rolü veriyoruz.
        user.setRoles(List.of(Role.USER));

        return userRepository.save(user);
    }

}
