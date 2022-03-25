package com.user.users.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTValidateFilter extends BasicAuthenticationFilter {

  public static final String HEADER_ATTRIBUTE = "Authorization";
  public static final String ATTRIBUTE_PREFIX = "Bearer ";

  public JWTValidateFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain
  )
    throws IOException, ServletException {
    String attribute = request.getHeader(HEADER_ATTRIBUTE);

    if (attribute == null) {
      chain.doFilter(request, response);
      return;
    }

    if (!attribute.startsWith(ATTRIBUTE_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }

    String token = attribute.replace(ATTRIBUTE_PREFIX, "");

    UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(
      token
    );

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    chain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthenticationToken(
    String token
  ) {
    String user = JWT
      .require(Algorithm.HMAC512(JWTAuthenticationFilter.TOKEN_SENHA))
      .build()
      .verify(token)
      .getSubject();

    if (user == null) {
      return null;
    }

    return new UsernamePasswordAuthenticationToken(
      user,
      null,
      new ArrayList<>()
    );
  }
}
