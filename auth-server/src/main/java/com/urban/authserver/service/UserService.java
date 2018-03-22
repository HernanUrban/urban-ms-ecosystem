package com.urban.authserver.service;


import com.urban.authserver.domain.User;
import com.urban.authserver.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> account = userRepo.findByUsername(s);
        if (account.isPresent()) {
            return account.get();
        } else {
            throw new UsernameNotFoundException(String.format("Username[%s] not found", s));
        }
    }

    public User findAccountByUsername(String username) throws UsernameNotFoundException {
        Optional<User> account = userRepo.findByUsername(username);
        if (account.isPresent()) {
            return account.get();
        } else {
            throw new UsernameNotFoundException(String.format("Username[%s] not found", username));
        }

    }

    public User create(User user) throws AccountException {
        if (userRepo.countByUsername(user.getUsername()) == 0) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepo.save(user);
        } else {
            throw new AccountException(String.format("Username[%s] already exists.", user.getUsername()));
        }
    }

    @Transactional // To successfully remove the date @Transactional annotation must be added
    public void deleteCurrentUser() throws UsernameNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findAccountByUsername(username);
        userRepo.deleteAccountById(user.getId());

    }
}
