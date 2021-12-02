package com.epam.esm.security.filter;

import com.epam.esm.security.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Filter that is responsible for authorization.
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String LOGIN_END_POINT = "/login";
    private static final String SIGN_UP_END_POINT = "/users/signup";
    private final static String AUTHENTICATION_SCHEME = "Bearer ";

    public CustomAuthorizationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (requireAuthorization(request)){
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(AUTHENTICATION_SCHEME)){
                String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length());
                jwtTokenProvider.validateToken(token);
                Authentication authenticationToken = jwtTokenProvider.receiveUserAuthenticationFromDatabase(token);
                if (authenticationToken==null){
                    authenticationToken = jwtTokenProvider.receiveAuthenticationByTokenFromMap(token);
                }
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean requireAuthorization(HttpServletRequest request){
        return !request.getServletPath().equals(LOGIN_END_POINT) || request.getServletPath().equals(SIGN_UP_END_POINT);
    }
}
