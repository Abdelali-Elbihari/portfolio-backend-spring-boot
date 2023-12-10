package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.dto.UserDto;
import com.abdelalielbihari.portfolio.model.User;
import com.abdelalielbihari.portfolio.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Optional<User> getUserByName(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public User createUser(UserDto userdto) {
    return userRepository.save(User.builder()
        .username(userdto.getUsername())
        .password(passwordEncoder.encode(userdto.getPassword()))
        .roles(userdto.getRoles())
        .email(userdto.getEmail())
        .build());
  }
}
