package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.dto.UserDto;
import com.abdelalielbihari.portfolio.model.User;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

  User createUser(UserDto userdto);

  Optional<User> getUserByName(String username);
}
