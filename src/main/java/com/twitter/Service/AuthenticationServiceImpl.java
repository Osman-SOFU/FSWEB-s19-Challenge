package com.twitter.Service;

import com.twitter.Entity.Role;
import com.twitter.Entity.User;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Repository.RoleRepository;
import com.twitter.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        if (authentication.isAuthenticated()) {
            return "Hoş geldiniz, " + email + "!"; // ✅ Kullanıcıya hoş geldiniz mesajı
        } else {
            throw new RuntimeException("Kimlik doğrulama başarısız!");
        }
    }

    @Override
    public User register(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()){
            throw new TwitterException("Email already registered", HttpStatus.BAD_GATEWAY);
        }

        String encodedPassword = passwordEncoder.encode(password);

        Optional<Role> userRole = roleRepository.findByAuthority("USER");

        if(userRole.isEmpty()){
            Role role = new Role();
            role.setAuthority("USER");
            userRole = Optional.of(roleRepository.save(role));
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setName("Varsayılan Ad"); // ✅ `name` set edildi
        user.setSurname("Varsayılan Soyad"); // ✅ `surname` set edildi
        user.setAuthorities(Set.of(userRole.get()));

        return userRepository.save(user);
    }
}
