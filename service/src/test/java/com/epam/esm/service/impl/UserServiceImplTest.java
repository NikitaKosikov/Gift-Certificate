package com.epam.esm.service.impl;

import com.epam.esm.config.ServiceConfiguration;
import com.epam.esm.dto.impl.RoleDto;
import com.epam.esm.dto.impl.SearchByParametersDto;
import com.epam.esm.dto.impl.TagDto;
import com.epam.esm.dto.impl.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CustomResourceNotFoundException;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.specification.SearchByParametersSpecification;
import com.epam.esm.specification.model.SearchByParameters;
import com.epam.esm.validator.UserValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ServiceConfiguration.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    private UserService userService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    private static User user1;
    private static User user2;
    private static User user3;
    private static UserDto userDto1;
    private static UserDto userDto2;
    private static UserDto userDto3;
    private static Pageable pageable;
    private static Page<User> userPage;
    private static List<User> users;
    private static List<UserDto> usersDto;
    private static SearchByParametersDto searchByParametersDto;
    private static SearchByParameters searchByParameters;

    @BeforeEach
    public void initUseCase(){
        userService = new UserServiceImpl(userRepository, modelMapper, userValidator, passwordEncoder);
    }

    @BeforeAll
    public static void setUp(){
        user1 = new User(1L, "user1", "password", Role.USER);
        user2 = new User(null, "user2", "password", Role.USER);
        user3 = new User(2L, "user3", "password", null);
        userDto1 = new UserDto(1L, "user1", "password", RoleDto.USER);
        userDto2 = new UserDto(null, "user1", "password", RoleDto.USER);
        userDto3 = new UserDto(2L, "user1", "password", RoleDto.USER);
        pageable = PageRequest.of(1,5, Sort.unsorted());
        users = List.of(user1, user2);
        usersDto = List.of(userDto1, userDto2);
        searchByParametersDto = new SearchByParametersDto(new HashMap<>(), new ArrayList<>(), Sort.unsorted());
        searchByParameters = new SearchByParameters();
        userPage = new PageImpl<>(List.of(user1, user2));
    }

    @AfterAll
    public static void tearDown(){
        user1 = null;
        user2 = null;
        user3 = null;
        userDto1 = null;
        userDto2 = null;
        userDto3 = null;
        pageable = null;
        users = null;
        usersDto = null;
    }

    @Test
    void findAll() {
        Type type = new TypeToken<List<UserDto>>() {}.getType();
        when(modelMapper.map(searchByParametersDto, SearchByParameters.class)).thenReturn(searchByParameters);
        when(userRepository.findAll(isA(SearchByParametersSpecification.class), isA(Pageable.class))).thenReturn(userPage);
        when(modelMapper.map(users, type)).thenReturn(usersDto);

        List<UserDto> actualList = userService.findAll(pageable, searchByParametersDto);

        assertEquals(actualList, usersDto);
    }

    @Test
    void findByIdPositiveResult() {
        final Long id = 1L;
        doNothing().when(userValidator).validateId(anyLong());
        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        when(modelMapper.map(user1, UserDto.class)).thenReturn(userDto1);

        UserDto actualUserDto = userService.findById(id);

        assertEquals(userDto1, actualUserDto);
    }

    @Test
    void findByIdNotFoundUserException() {
        final Long id = 100000L;
        doNothing().when(userValidator).validateId(anyLong());
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class, () -> userService.findById(id));
    }

    @Test
    void findByIdIncorrectIdException() {
        final Long id = -200L;
        doThrow(new IncorrectParameterValueException()).when(userValidator).validateId(id);
        assertThrows(IncorrectParameterValueException.class, () -> userService.findById(id));
    }


    @Test
    void savePositiveTest() {
        doNothing().when(userValidator).validate(userDto2);
        when(modelMapper.map(userDto2, User.class)).thenReturn(user2);
        when(userRepository.save(user2)).thenReturn(user3);
        when(modelMapper.map(user3, UserDto.class)).thenReturn(userDto3);

        UserDto actualUserDto = userService.save(userDto2);

        assertEquals(userDto3, actualUserDto);
    }

    @Test
    void UserValidatorInvalidUsername() {
        UserDto userDtoUsernameIsNull = new UserDto(null, null, "password", RoleDto.USER);
        UserDto userDtoUsernameIsBlank = new UserDto(null, "", "password", RoleDto.USER);
        UserDto userDtoUsernameIsNotMatcherPattern = new UserDto(null, "23412", "password", RoleDto.USER);

        doThrow(new IncorrectParameterValueException()).when(userValidator).validate(userDtoUsernameIsNull);
        doThrow(new IncorrectParameterValueException()).when(userValidator).validate(userDtoUsernameIsBlank);
        doThrow(new IncorrectParameterValueException()).when(userValidator).validate(userDtoUsernameIsNotMatcherPattern);

        assertThrows(IncorrectParameterValueException.class, () -> userService.save(userDtoUsernameIsNull));
        assertThrows(IncorrectParameterValueException.class, () -> userService.save(userDtoUsernameIsBlank));
        assertThrows(IncorrectParameterValueException.class, () -> userService.save(userDtoUsernameIsNotMatcherPattern));
    }

    @Test
    void UserValidatorInvalidPassword() {
        UserDto userDtoPasswordIsNull = new UserDto(null, "user1", null, RoleDto.USER);
        UserDto userDtoPasswordIsBlank = new UserDto(null, "user1", "", RoleDto.USER);
        UserDto userDtoPasswordIsTooSmall = new UserDto(null, "user1", "asd", RoleDto.USER);
        UserDto userDtoPasswordIsTooBig = new UserDto(null, "user1", "sadnkasl123qweeqwe", RoleDto.USER);

        doThrow(new IncorrectParameterValueException()).when(userValidator).validate(userDtoPasswordIsNull);
        doThrow(new IncorrectParameterValueException()).when(userValidator).validate(userDtoPasswordIsBlank);
        doThrow(new IncorrectParameterValueException()).when(userValidator).validate(userDtoPasswordIsTooSmall);
        doThrow(new IncorrectParameterValueException()).when(userValidator).validate(userDtoPasswordIsTooBig);

        assertThrows(IncorrectParameterValueException.class, () -> userService.save(userDtoPasswordIsNull));
        assertThrows(IncorrectParameterValueException.class, () -> userService.save(userDtoPasswordIsBlank));
        assertThrows(IncorrectParameterValueException.class, () -> userService.save(userDtoPasswordIsTooSmall));
        assertThrows(IncorrectParameterValueException.class, () -> userService.save(userDtoPasswordIsTooBig));
    }

    @Test
    void findByUsernamePositiveTest() {
        String username = "user3";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user3));

        Optional<User> actualUser = userService.findByUsername(username);

        assertTrue(actualUser.isPresent());
        assertEquals(username, actualUser.get().getUsername());
    }
}