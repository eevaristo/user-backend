package com.user.users.services;

import java.util.Optional;

import com.user.users.model.data.UserDetailsData;
import com.user.users.model.entity.UserAccount;
import com.user.users.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

  @Autowired
  private UserAccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
      Optional<UserAccount> userAccount = accountRepository.findByEmail(username);
    if (userAccount.isPresent()) {
      return new UserDetailsData(userAccount);
    }
    throw new UsernameNotFoundException("Dados Inválidos!");
  }
}
