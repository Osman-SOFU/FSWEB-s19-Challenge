package com.twitter.Service;

import com.twitter.Entity.User;

public interface AuthenticationService {
    String login(String email, String password);
    User register(User user);
}