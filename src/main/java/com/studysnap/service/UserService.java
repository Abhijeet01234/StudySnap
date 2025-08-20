package com.studysnap.service;

import com.studysnap.model.User;

public interface UserService {
    User saveUser(User user);
    User getUserByEmail(String email);
}
