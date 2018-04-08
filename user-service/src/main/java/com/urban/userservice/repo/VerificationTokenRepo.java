package com.urban.userservice.repo;

import com.urban.userservice.domain.User;
import com.urban.userservice.domain.VerificationToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {

  Optional<VerificationToken> findByToken(String token);

  Optional<VerificationToken> findByUser(User user);
}
