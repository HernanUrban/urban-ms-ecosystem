package com.urban.authserver.repo;


import com.urban.authserver.domain.User;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepo extends Repository<User, Long> {

    Collection<User> findAll();

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    Integer countByUsername(String username);

    User save(User user);

    void deleteAccountById(Long id);


}
