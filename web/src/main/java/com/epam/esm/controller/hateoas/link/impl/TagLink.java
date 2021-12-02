package com.epam.esm.controller.hateoas.link.impl;

import com.epam.esm.controller.hateoas.assembler.TagDtoAssembler;
import com.epam.esm.controller.hateoas.model.TagDtoModel;
import com.epam.esm.controller.hateoas.link.CustomLink;
import com.epam.esm.dto.impl.TagDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Class is implementation of interface {@link CustomLink} and
 * intended to work with tag links
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Component
public class TagLink implements CustomLink<TagDtoModel, TagDto> {

    private static final String CREATE_LINK = "create";
    private static final String UPDATE_LINK = "update";
    private static final String DELETE_LINK = "delete";

    @Override
    public TagDtoModel toLink(TagDto entity) {
        return new TagDtoAssembler().toModel(entity);
    }

    @Override
    public CollectionModel<TagDtoModel> toLink(List<TagDto> entities) {
        if (entities!=null){
            CollectionModel<TagDtoModel> tagDtoModelCollectionModel = new TagDtoAssembler().toCollectionModel(entities);
            return CollectionModel.of(tagDtoModelCollectionModel);
        }
        return CollectionModel.empty();
    }
}
