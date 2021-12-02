package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.model.UserDtoModel;
import com.epam.esm.controller.hateoas.link.CustomLink;
import com.epam.esm.converter.ParametersToDtoConverter;
import com.epam.esm.dto.impl.SearchByParametersDto;
import com.epam.esm.dto.impl.TokenDto;
import com.epam.esm.dto.impl.UserDto;
import com.epam.esm.security.UserDetailsServiceImpl;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Class is an endpoint for user authentication and registration
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@RestController
@RequestMapping("/users")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserController {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailService;
    private final ParametersToDtoConverter parametersToDtoConverter;
    private final CustomLink<UserDtoModel, UserDto> userDtoCustomLink;

    @Autowired
    public UserController(UserService userService, UserDetailsServiceImpl userDetailService, ParametersToDtoConverter parametersToDtoConverter, CustomLink<UserDtoModel, UserDto> userDtoCustomLink){
        this.userService = userService;
        this.userDetailService = userDetailService;
        this.parametersToDtoConverter = parametersToDtoConverter;
        this.userDtoCustomLink = userDtoCustomLink;
    }

    /**
     * Find users by parameters, processes GET requests at
     * /users
     *
     * @param pageable the contains information about the data selection.
     * @param parameters the tag search parameters.
     * @return the responseEntity object with contains HTTP status (200)
     * and found users as CollectionModel<UserDtoModel>.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<UserDtoModel>> findAll(Pageable pageable,
                                                 @RequestParam(required = false) Map<String, String> parameters){
        SearchByParametersDto searchByParametersDto = parametersToDtoConverter.convert(parameters);
        List<UserDto> users = userService.findAll(pageable, searchByParametersDto);
        CollectionModel<UserDtoModel> userDtoModelCollectionModel = userDtoCustomLink.toLink(users);
        return new ResponseEntity<>(userDtoModelCollectionModel, HttpStatus.OK);
    }

    /**
     * Find user by id, processes GET requests at /users/{id}.
     *
     * @param id the user id which will be found.
     * @return the responseEntity object with contains HTTP status (OK)
     * and the found user as UserDtoModel.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDtoModel> findById(@PathVariable Long id){
        UserDto userDto = userService.findById(id);
        UserDtoModel userDtoModel = userDtoCustomLink.toLink(userDto);
        return new ResponseEntity<>(userDtoModel, HttpStatus.OK);
    }

    /**
     * Create new user, processes POST requests at /signup.
     *
     * @param userDto the new user as dto which will be created.
     * @return the responseEntity object with contains HTTP status (CREATED)
     * and the TokenDto object.
     */
    @PostMapping("/signup")
    @PreAuthorize("permitAll()")
    public ResponseEntity<TokenDto> signup(@RequestBody UserDto userDto) {
        TokenDto tokenDto = userDetailService.signUp(userDto);
        return new ResponseEntity<>(tokenDto, HttpStatus.CREATED);
    }

}
