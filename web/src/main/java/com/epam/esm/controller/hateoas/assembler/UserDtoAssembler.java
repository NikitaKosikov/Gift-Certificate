package com.epam.esm.controller.hateoas.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.controller.hateoas.model.UserDtoModel;
import com.epam.esm.dto.impl.UserDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

/**
 * Class that sets a links to our UserDtoModel
 *
 * @author Nikita Kosikov
 * @version 1.0
 * @see org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
 */
public class UserDtoAssembler extends RepresentationModelAssemblerSupport<UserDto, UserDtoModel> {

    public UserDtoAssembler() {
        super(UserController.class, UserDtoModel.class);
    }

    @Override
    protected UserDtoModel instantiateModel(UserDto entity) {
        return new UserDtoModel(entity);
    }

    /**
     * Sets a links to our UserDtoModel.
     *
     * @param userDto the object in which will be added links.
     * @return the UserDtoModel this contains links.
     */
    @Override
    public UserDtoModel toModel(UserDto userDto) {
        return createModelWithId(userDto.getId(), userDto);
    }
}
