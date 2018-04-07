package com.urban.userservice.repo;

import com.urban.userservice.domain.User;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface UserRepo extends Repository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findById(Long id);

  User save(User user);

  void deleteById(Long id);


}
