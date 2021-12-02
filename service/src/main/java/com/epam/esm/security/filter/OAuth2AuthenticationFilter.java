package com.epam.esm.security.filter;

import com.epam.esm.security.JwtTokenProvider;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.oidc.authentication.OidcAuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Filter that is responsible for authenticating the OAuth2 login.
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
public class OAuth2AuthenticationFilter extends GenericFilterBean {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String REQUIRE_URI= "/gift-certificates";
    private static final String BASE_URI = "/auth/realms";
    private static final String CLIENT_ID = "springboot-keycloak";
    private static final String AUTHORIZATION_CODE_PARAMETER_NAME = "code";
    private static final String REDIRECT_URI = "http://localhost:8081/gift-certificates";
    private static final String NONCE_PARAMETER_NAME = "nonce";
    private static final String USERNAME_ATTRIBUTE_NAME = "username";
    private static final String OAUTH2_ROLES_NAME = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    private AuthenticationManager authenticationManager;


    public OAuth2AuthenticationFilter(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository, JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClientRepository = authorizedClientRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void initFilterBean() throws ServletException {
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        OidcUserService userService = new OidcUserService();
        OidcAuthorizationCodeAuthenticationProvider authenticationProvider = new OidcAuthorizationCodeAuthenticationProvider(accessTokenResponseClient, userService);
        authenticationManager = new ProviderManager(authenticationProvider);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        initFilterBean();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (!requireAuthentication(httpServletRequest)){
            chain.doFilter(request, response);
            return;
        }

        try {
            OAuth2AuthenticationToken authentication = authenticate(httpServletRequest, httpServletResponse);
            successfulAuthentication(httpServletResponse, authentication);
        }catch (Exception e){
            unsuccessfulAuthentication(httpServletResponse, e);
        }
    }

    private boolean requireAuthentication(HttpServletRequest request){
        return request.getServletPath().equals(REQUIRE_URI);
    }


    private OAuth2AuthenticationToken authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String code = request.getParameter(AUTHORIZATION_CODE_PARAMETER_NAME);
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(CLIENT_ID);

        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, BASE_URI);
        authorizationRequestResolver.setAuthorizationRequestCustomizer(req -> req
                .redirectUri(REDIRECT_URI)
                .additionalParameters(param -> param.remove(NONCE_PARAMETER_NAME))
                .attributes(attr -> attr.remove(NONCE_PARAMETER_NAME))

        );

        OAuth2AuthorizationRequest authorizationRequest = authorizationRequestResolver.resolve(request, CLIENT_ID);

        OAuth2AuthorizationResponse authorizationResponse = OAuth2AuthorizationResponse
                .success(code)
                .redirectUri(REDIRECT_URI)
                .state(authorizationRequest.getState())
                .build();

        OAuth2LoginAuthenticationToken authenticationRequest =
                new OAuth2LoginAuthenticationToken(clientRegistration, new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse));

        OAuth2LoginAuthenticationToken authenticationResult = (OAuth2LoginAuthenticationToken) authenticationManager.authenticate(authenticationRequest);

        String username = authenticationResult.getPrincipal().getAttribute(USERNAME_ATTRIBUTE_NAME);

        UserDetails user = loadUser(username);
        Collection<GrantedAuthority> authorities = mergeAuthorities(authenticationResult, user);

        OAuth2AuthenticationToken oauth2Authentication = new OAuth2AuthenticationToken(
                authenticationResult.getPrincipal(),
                authorities,
                authenticationResult.getClientRegistration().getRegistrationId());

        OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(
                authenticationResult.getClientRegistration(),
                oauth2Authentication.getName(),
                authenticationResult.getAccessToken(),
                authenticationResult.getRefreshToken());
        authorizedClientRepository.saveAuthorizedClient(authorizedClient, oauth2Authentication, request, response);
        return oauth2Authentication;
    }

    private UserDetails loadUser(String username){
        try {
            return userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }

    private Collection<GrantedAuthority> mergeAuthorities(OAuth2LoginAuthenticationToken authentication, UserDetails user) {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        if (user!=null){
            authorities.addAll(user.getAuthorities());
        }
        authentication.getAuthorities()
                .forEach(grantedAuthority -> {
                    if (grantedAuthority instanceof OidcUserAuthority oidcUserAuthority1){
                        authorities.addAll(receiveAuthoritiesOAuth2Roles(oidcUserAuthority1));
                    }else if (grantedAuthority instanceof SimpleGrantedAuthority){
                        authorities.add(grantedAuthority);
                    }
                });
        return authorities;
    }


    private Collection<SimpleGrantedAuthority> receiveAuthoritiesOAuth2Roles(OidcUserAuthority oidcUserAuthority){
        OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();
        if (userInfo.hasClaim(OAUTH2_ROLES_NAME)){
            List<String> roles = userInfo.getClaimAsStringList(OAUTH2_ROLES_NAME);
            return generateAuthoritiesOAuth2Roles(roles);
        }
        return Collections.emptyList();
    }

    private Collection<SimpleGrantedAuthority> generateAuthoritiesOAuth2Roles(List<String> roles){
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role));
        }
        return authorities;
    }

    private void successfulAuthentication(HttpServletResponse response, OAuth2AuthenticationToken authentication) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateOAuth2Token(authentication);
        jwtTokenProvider.relateTokenWithAuthentication(token, authentication);
        response.addHeader(CONTENT_TYPE_HEADER, MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().println(token);
    }

    private void unsuccessfulAuthentication(HttpServletResponse response, Exception exception) throws IOException {
        SecurityContextHolder.clearContext();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getLocalizedMessage());
    }
}
