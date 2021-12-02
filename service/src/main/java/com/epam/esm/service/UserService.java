package com.epam.esm.service;

import com.epam.esm.dto.impl.SearchByParametersDto;
import com.epam.esm.dto.impl.UserDto;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
public interface UserService {

    /**
     * Looking for a set of users as dto.
     *
     * @param pageable the contains information about the data selection.
     * @param searchByParametersDto the object that contains sorting and filter by fields.
     * @return list of found users as dto or List#empty() if none found.
     */
    List<UserDto> findAll(Pageable pageable, SearchByParametersDto searchByParametersDto);

    /**
     * Looking for the user as dto by specific id.
     *
     * @param id the id of user.
     * @return the found user as dto or throw CustomResourceNotFoundException if none found.
     */
    UserDto findById(Long id);

    /**
     * Create user.
     *
     * @param userDto the user as dto by which order will be created.
     * @return the created user as dto.
     */
    UserDto save(UserDto userDto);

    /**
     * Looking for the user by username.
     *
     * @param username the username of user.
     * @return the found user or Optional#empty() if none found.
     */
    Optional<User> findByUsername(String username);
}
