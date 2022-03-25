package com.user.users.controller;

import com.user.users.model.entity.UserAccount;
import com.user.users.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserAccountController {

  @Autowired
  private UserAccountService accountService;

  @PostMapping
  public ResponseEntity<Object> createUser(
    @RequestBody UserAccount userAccount
  ) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(accountService.saveUser(userAccount));
  }
}
