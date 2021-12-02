package com.epam.esm.security;

import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Class contains operations with JSON web tokens
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_IN_TOKEN = "authorities";
    private final static String USERNAME_ATTRIBUTE = "preferred_username";

    private final UserService userService;

    private final Map<String, OAuth2AuthenticationToken> auth2AuthenticationTokenMap = new HashMap<>();

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expirationToken}")
    private long expirationToken;

    @Autowired
    public JwtTokenProvider(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * Generate token.
     *
     * @param user the user from which the token will be created.
     * @return the token.
     */
    public String generateToken(org.springframework.security.core.userdetails.User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim(AUTHORITIES_IN_TOKEN, user.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationToken))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Generate OAuth2 token.
     *
     * @param authentication the authentication from which the token will be created.
     * @return the token.
     */
    public String generateOAuth2Token(OAuth2AuthenticationToken authentication){
        String username = authentication.getPrincipal().getAttribute(USERNAME_ATTRIBUTE);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return Jwts.builder()
                .setSubject(username)
                .claim(AUTHORITIES_IN_TOKEN, authorities)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setExpiration(new Date(System.currentTimeMillis() + expirationToken))
                .compact();
    }


    /**
     * Validate token.
     *
     * @param token the token for validation.
     * @throws JwtException if the token is invalid.
     * @return true if token is valid and false if token is invalid.
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Receive user authentication from database.
     *
     * @param token the user's token
     * @return Authentication object or null if user not found in database.
     */
    public Authentication receiveUserAuthenticationFromDatabase(String token) {
        String username = getUsername(token);
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()){

            UserDetails userDetails = UserDetailsFactory.create(user.get());
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        return null;
    }

    /**
     * Get OAuth2AuthenticationToken object by token.
     *
     * @param token the token by which we receive our OAuth2AuthenticationToken object.
     * @return found OAuth2AuthenticationToken object.
     */
    public OAuth2AuthenticationToken receiveAuthenticationByTokenFromMap(String token) {
        return auth2AuthenticationTokenMap.get(token);
    }

    /**
     * Relate token with OAuth2AuthenticationToken object.
     *
     * @param token the token that will be related with authentication.
     * @param authentication the authentication that will be related with token.
     */
    public void relateTokenWithAuthentication(String token, Authentication authentication){
        auth2AuthenticationTokenMap.put(token, (OAuth2AuthenticationToken) authentication);
    }

    /**
     * Get username from token.
     *
     * @param token the token from which we receive username.
     * @return found username.
     */
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Get token expiration time.
     *
     * @return expiration time.
     */
    public long getExpirationToken() {
        return expirationToken;
    }
}
