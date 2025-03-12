package com.twitter.Service;

import com.twitter.Entity.User;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // SecurityConfig'deki PasswordEncoder kullanılıyor


    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            return optionalUser.get();
        }

        throw new TwitterException(id +"'li kullanıcı bulunamadı", HttpStatus.NOT_FOUND);
    }

    @Override
    public User save(User user) {
        boolean userExists = userRepository.existsByEmail(user.getEmail());
        if (userExists) {
            throw new TwitterException(user.getEmail() + " e-posta adresi zaten kayıtlı", HttpStatus.BAD_REQUEST);
        }

        // Şifreyi bcrypt ile şifrele
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new TwitterException(id + "'li kullanıcı bulunamadı", HttpStatus.NOT_FOUND));

        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }

        if (user.getSurname() != null) {
            existingUser.setSurname(user.getSurname());
        }

        if (user.getEmail() != null && !existingUser.getEmail().equals(user.getEmail())) {
            boolean emailExists = userRepository.existsByEmail(user.getEmail());
            if (emailExists) {
                throw new TwitterException("Bu e-posta adresi zaten kullanımda!", HttpStatus.BAD_REQUEST);
            }
            existingUser.setEmail(user.getEmail());
        }

        if (user.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new TwitterException("Kullanıcı bulunamadı!", HttpStatus.NOT_FOUND));
    }
}
