package com.urban.userservice.service;

import com.urban.userservice.converter.UserResponseConverter;
import com.urban.userservice.domain.User;
import com.urban.userservice.domain.UserType;
import com.urban.userservice.domain.VerificationToken;
import com.urban.userservice.dto.Event;
import com.urban.userservice.dto.NewUserRequest;
import com.urban.userservice.dto.UserResponse;
import com.urban.userservice.error.DefaultErrorCodes;
import com.urban.userservice.error.UserNotFoundError;
import com.urban.userservice.error.UserServiceError;
import com.urban.userservice.error.UserValidationError;
import com.urban.userservice.event.EventPublisher;
import com.urban.userservice.repo.UserRepo;
import com.urban.userservice.repo.VerificationTokenRepo;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.security.auth.login.AccountException;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
  private VerificationTokenRepo tokenRepo;

  @Autowired
  private EventPublisher publisher;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserResponseConverter userConverter;

  @Value("${activate.endpoint.url}")
  private String activationUrl;

  public UserResponse findById(Long id) throws UserValidationError {
    Optional<User> user = userRepo.findById(id);
    if (user.isPresent()) {
      return userConverter.convert(user.get());
    } else {
      throw new UserValidationError(DefaultErrorCodes.INVALID_ARGUMENTS, String.format("User [%d] not found", id));
    }
  }

  public UserResponse findByUsername(String username) throws UsernameNotFoundException, UserValidationError {
    Optional<User> account = userRepo.findByUsername(username);
    if (account.isPresent()) {
      return userConverter.convert(account.get());
    } else {
      throw new UserValidationError(String.format("Username[%s] not found",
          username));
    }
  }

  public UserResponse create(NewUserRequest userRequest, UserType type) throws UserServiceError {
    UserResponse userResponse = null;
    String token = null;
    if (!userRepo.findByUsername(userRequest.getUsername()).isPresent()) {
      User user = new User();
      user.grantAuthority("ROLE_" + type.name());
      user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
      user.setEmail(userRequest.getEmail());
      user.setFirstName(userRequest.getFirstName());
      user.setLastName(userRequest.getLastName());
      user.setUsername(userRequest.getUsername());
      User newUser = userRepo.save(user);
      token = createVerificationToken(newUser);
      userResponse = userConverter.convert(newUser);
    } else {
      throw new UserValidationError(DefaultErrorCodes.INVALID_ARGUMENTS, String.format("Username[%s] already exists.", userRequest
          .getUsername()));
    }
    publisher.send(new Event().setUserId(userResponse.getId().toString())
                              .setCreationDate(new Date())
                              .setEmail(userResponse.getEmail())
                              .setNotificationType("create_user")
                              .setResourceId("user-service")
                              .addAttribute("first_name", userRequest.getFirstName())
                              .addAttribute("action_url", String.format(activationUrl, token)));
    return userResponse;
  }

  @Transactional
  public void deleteCurrentUser() throws UserNotFoundError {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepo.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundError(DefaultErrorCodes.ENTITY_NOT_FOUND, String.format("Username[%s] not found",
            username)));
    userRepo.deleteById(user.getId());
  }

  public String createVerificationToken(String username) {
    String token = null;
    Optional<User> user = userRepo.findByUsername(username);
    if (user.isPresent()) {
      token = createVerificationToken(user.get());
    }
    return token;
  }

  public String createVerificationToken(User user) {
    String token = UUID.randomUUID().toString();
    VerificationToken myToken = new VerificationToken(token, user);
    VerificationToken verificationToken = tokenRepo.save(myToken);
    return verificationToken.getToken();
  }

  public User getUserByVerificationToken(String verificationToken) {
    User user = null;
    Optional<VerificationToken> token = tokenRepo.findByToken(verificationToken);
    if (token.isPresent()) {
      user = token.get().getUser();
    }
    return user;
  }

  public Optional<VerificationToken> getVerificationToken(String token) {
    return tokenRepo.findByToken(token);
  }

  public void verifyUserEmail(String token) throws UserServiceError {
    Optional<VerificationToken> vToken = getVerificationToken(token);
    if (!vToken.isPresent()) {
      throw new UserServiceError("Invalid Token!");
    }
    Calendar cal = Calendar.getInstance();
    VerificationToken verifToken = vToken.get();
    if ((verifToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
      throw new UserServiceError("Token has expired!");
    }
    User user = userRepo.findById(verifToken.getUser().getId()).get();
    user.setEnabled(true);
    userRepo.save(user);
  }
}
