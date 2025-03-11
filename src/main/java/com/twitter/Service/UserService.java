package com.twitter.Service;

import com.twitter.Entity.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User findById(Long id);
    User findByEmail(String email);
    User save(User user);
    User update(Long id, User user);
    void delete(Long id);
}
