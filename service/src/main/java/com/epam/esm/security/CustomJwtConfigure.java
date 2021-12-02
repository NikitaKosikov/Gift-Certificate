package com.epam.esm.security;

import com.epam.esm.security.filter.CustomAuthenticationFilter;
import com.epam.esm.security.filter.CustomAuthorizationFilter;
import com.epam.esm.security.filter.OAuth2AuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * Class configure http security
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Component
public class CustomJwtConfigure extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;
    private final UserDetailsService userDetailsService;


    public CustomJwtConfigure(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClientRepository = authorizedClientRepository;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configure http security.
     *
     * @param httpSecurity the httpSecurity.
     */
    @Override
    public void configure(HttpSecurity httpSecurity) {
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(authenticationManager, jwtTokenProvider);
        CustomAuthorizationFilter authorizationFilter = new CustomAuthorizationFilter(jwtTokenProvider);
        OAuth2AuthenticationFilter OAuth2AuthenticationFilter = new OAuth2AuthenticationFilter(clientRegistrationRepository, authorizedClientRepository, jwtTokenProvider, userDetailsService);
        httpSecurity.addFilter(authenticationFilter);
        httpSecurity.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(OAuth2AuthenticationFilter, BasicAuthenticationFilter.class);
    }
}
