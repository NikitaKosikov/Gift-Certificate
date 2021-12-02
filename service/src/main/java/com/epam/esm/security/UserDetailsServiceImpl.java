package com.epam.esm.security;

import com.epam.esm.dto.impl.TokenDto;
import com.epam.esm.dto.impl.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Class is implementation of interface {@link UserDetailsService} and
 * intended to work with user
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper;

    public UserDetailsServiceImpl(UserRepository userRepository, UserService userService, JwtTokenProvider jwtTokenProvider, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.modelMapper = modelMapper;
    }

    /**
     * Load user by username
     *
     * @param username the username of user by which will be found user from database
     * @return security user
     * @throws UsernameNotFoundException if user not found in database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.get().getRole().name()));
        return new org.springframework.security.core.userdetails.User(
                username,
                user.get().getPassword(),
                authorities);
    }

    /**
     * Creating user.
     *
     * @param userDto the new user as dto which will be created.
     * @return TokenDto object that contains information about user and token.
     */
    public TokenDto signUp(UserDto userDto) {
        userDto = userService.save(userDto);
        User user = modelMapper.map(userDto, User.class);
        user.setRole(Role.USER);
        UserDetails userDetails = UserDetailsFactory.create(user);
        String token = jwtTokenProvider.generateToken((org.springframework.security.core.userdetails.User) userDetails);
        return new TokenDto(user.getUsername(), token, jwtTokenProvider.getExpirationToken());
    }
}
