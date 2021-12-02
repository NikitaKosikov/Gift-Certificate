package com.epam.esm.service.impl;

import com.epam.esm.dto.impl.RoleDto;
import com.epam.esm.entity.Role;
import com.epam.esm.specification.model.SearchByParameters;
import com.epam.esm.dto.impl.SearchByParametersDto;
import com.epam.esm.dto.impl.UserDto;
import com.epam.esm.exception.CustomResourceNotFoundException;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.specification.SearchByParametersSpecification;
import com.epam.esm.service.UserService;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.MessageKey;
import com.epam.esm.validator.UserValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Class is implementation of interface {@link UserService} and intended to work
 * with user
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, UserValidator userValidator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> findAll(Pageable pageable, SearchByParametersDto searchByParametersDto) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), searchByParametersDto.getSort());
        SearchByParameters searchByParameters = modelMapper.map(searchByParametersDto, SearchByParameters.class);
        Page<User> usersPage = userRepository.findAll(new SearchByParametersSpecification<>(searchByParameters), pageable);
        return modelMapper.map(usersPage.getContent(), new TypeToken<List<UserDto>>() {}.getType());
    }


    @Override
    public UserDto findById(Long id) throws CustomResourceNotFoundException {
        userValidator.validateId(id);
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(user -> modelMapper.map(user, UserDto.class))
                .orElseThrow(() -> new CustomResourceNotFoundException("no user by id",
                        MessageKey.USER_NOT_FOUND_BY_ID, String.valueOf(id), ErrorCode.USER.getCode()));
    }

    @Override
    public UserDto save(UserDto userDto) throws IncorrectParameterValueException {
        userValidator.validate(userDto);
        userDto.setRole(RoleDto.USER);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = modelMapper.map(userDto, User.class);
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
