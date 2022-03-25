package com.user.users.repositories;

import java.util.Optional;

import com.user.users.model.entity.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepository
  extends CrudRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmail(String email);
}
