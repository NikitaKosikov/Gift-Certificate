package com.epam.esm.security.filter;

import com.epam.esm.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter that is responsible for authenticating the login form.
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String USERNAME_FROM_REQUEST = "username";
    private static final String PASSWORD_FROM_REQUEST = "password";
    private static final String ACCESS_TOKEN_IN_HEADER = "access_token";

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter(USERNAME_FROM_REQUEST);
        String password = request.getParameter(PASSWORD_FROM_REQUEST);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String token = jwtTokenProvider.generateToken(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        jwtTokenProvider.relateTokenWithAuthentication(token, authentication);
        response.setHeader(ACCESS_TOKEN_IN_HEADER, token);
    }
}
