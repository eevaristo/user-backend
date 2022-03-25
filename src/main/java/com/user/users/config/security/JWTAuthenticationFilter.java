package com.user.users.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.users.model.data.UserDetailsData;
import com.user.users.model.entity.UserAccount;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter
  extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  public static final int TOKEN_EXPIRES = 3600000;

  public static final String TOKEN_SENHA =
    "29bef542-0974-4e3a-9b19-9a4544cf353f";

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request,
    HttpServletResponse response
  )
    throws AuthenticationException {
    try {
      UserAccount userAccount = new ObjectMapper()
      .readValue(request.getInputStream(), UserAccount.class);

      return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          userAccount.getEmail(),
          userAccount.getPassword(),
          new ArrayList<>()
        )
      );
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Falha ao autenticar usuario", e);
    }
  }

  @Override
  protected void successfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain,
    Authentication authResult
  )
    throws IOException, ServletException {
    UserDetailsData userDetailsData = (UserDetailsData) authResult.getPrincipal();

    String token = JWT
      .create()
      .withSubject(userDetailsData.getUsername())
      .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRES))
      .sign(Algorithm.HMAC512(TOKEN_SENHA));

      /* response.getWriter().write(userDetailsData.getUsername() + token);
      response.getWriter().flush(); */

      Map<String, String> tokens = new HashMap<>();
      tokens.put("Token", token);
      tokens.put("Email", userDetailsData.getUsername());

      response.setContentType("application/json");
      response.setHeader("Access-Control-Allow-Origin", "*");
      new ObjectMapper().writeValue(response.getOutputStream(), tokens);
  }
}
