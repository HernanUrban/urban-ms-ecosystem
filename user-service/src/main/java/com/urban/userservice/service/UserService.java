package com.urban.userservice.service;

import com.urban.userservice.converter.UserResponseConverter;
import com.urban.userservice.domain.User;
import com.urban.userservice.domain.UserType;
import com.urban.userservice.dto.NewUserRequest;
import com.urban.userservice.dto.UserResponse;
import com.urban.userservice.repo.UserRepo;
import java.util.Optional;
import javax.security.auth.login.AccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserResponseConverter userConverter;


  public UserResponse findByUsername(String username) throws UsernameNotFoundException {
    Optional<User> account = userRepo.findByUsername(username);
    if (account.isPresent()) {
      return userConverter.convert(account.get());
    } else {
      throw new IllegalArgumentException(String.format("Username[%s] not found",
          username));
    }
  }

  public UserResponse create(NewUserRequest userRequest, UserType type) throws AccountException {
    if (!userRepo.findByUsername(userRequest.getUsername()).isPresent()) {
      User user = new User();
      user.grantAuthority("ROLE_" + type.name());
      user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
      user.setEmail(userRequest.getEmail());
      user.setFirstName(userRequest.getFirstName());
      user.setLastName(userRequest.getLastName());
      user.setUsername(userRequest.getUsername());
      return userConverter.convert(userRepo.save(user));
    } else {
      throw new IllegalArgumentException(String.format("Username[%s] already exists.", userRequest
          .getUsername()));
    }
  }

  @Transactional
  public void deleteCurrentUser() throws UsernameNotFoundException {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepo.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(String.format("Username[%s] not found",
            username)));
    userRepo.deleteById(user.getId());
  }
}
