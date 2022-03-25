package com.user.users.services;

import com.user.users.model.entity.UserAccount;
import com.user.users.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {

  @Autowired
  private UserAccountRepository accountRepository;

  @Autowired
  private PasswordEncoder encoder;

  public UserAccount saveUser(UserAccount account) {
    account.setPassword(encoder.encode(account.getPassword()));
    return accountRepository.save(account);
  }
}
