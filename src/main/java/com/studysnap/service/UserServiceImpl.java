package com.studysnap.service;

import com.studysnap.model.User;
import com.studysnap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
