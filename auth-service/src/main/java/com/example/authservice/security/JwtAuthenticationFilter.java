package com.example.authservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.authservice.service.UserDetailsServiceImpl;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final UserDetailsServiceImpl userDetailsService;

  public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl uds) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = uds;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                  FilterChain chain) throws java.io.IOException, jakarta.servlet.ServletException {
    String header = req.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      if (jwtUtil.validateToken(token)) {
        String username = jwtUtil.getClaims(token).getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        var authToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    chain.doFilter(req, res);
  }
}
