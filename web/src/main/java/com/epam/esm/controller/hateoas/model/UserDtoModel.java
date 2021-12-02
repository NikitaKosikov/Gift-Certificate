package com.epam.esm.controller.hateoas.model;

import com.epam.esm.dto.impl.UserDto;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Relation(value = "user", collectionRelation = "users")
public class UserDtoModel extends RepresentationModel<UserDtoModel> {
    @Getter
    private final Long id;
    @Getter
    private final String username;
    @Getter
    private final String password;

    public UserDtoModel(UserDto userDto) {
        this.id = userDto.getId();
        this.username = userDto.getUsername();
        this.password = userDto.getPassword();
    }
}
