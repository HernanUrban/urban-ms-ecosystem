package com.urban.authserver.service;


import com.urban.authserver.converter.UserResponseConverter;
import com.urban.authserver.domain.User;
import com.urban.authserver.domain.UserType;
import com.urban.authserver.dto.NewUserRequest;
import com.urban.authserver.dto.UserResponse;
import com.urban.authserver.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountException;
import java.util.Optional;

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
            throw new UsernameNotFoundException(String.format("Username[%s] not found", username));
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
            throw new AccountException(String.format("Username[%s] already exists.", userRequest.getUsername()));
        }
    }

    @Transactional
    public void deleteCurrentUser() throws UsernameNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username[%s] not found", username)));
        userRepo.deleteById(user.getId());
    }
}
