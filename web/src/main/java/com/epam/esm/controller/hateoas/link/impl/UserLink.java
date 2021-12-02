package com.epam.esm.controller.hateoas.link.impl;

import com.epam.esm.controller.hateoas.assembler.UserDtoAssembler;
import com.epam.esm.controller.hateoas.model.UserDtoModel;
import com.epam.esm.controller.hateoas.link.CustomLink;
import com.epam.esm.dto.impl.UserDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class is implementation of interface {@link CustomLink} and
 * intended to work with user links
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Component
public class UserLink implements CustomLink<UserDtoModel, UserDto> {

    @Override
    public UserDtoModel toLink(UserDto entity) {
        return new UserDtoAssembler().toModel(entity);
    }

    @Override
    public CollectionModel<UserDtoModel> toLink(List<UserDto> entities) {
        if (entities!=null){
            return new UserDtoAssembler().toCollectionModel(entities);
        }
        return CollectionModel.empty();
    }
}
