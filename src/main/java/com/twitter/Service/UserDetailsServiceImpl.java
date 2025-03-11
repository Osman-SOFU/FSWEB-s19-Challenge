package com.twitter.Service;

import com.twitter.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        new ArrayList<>()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
