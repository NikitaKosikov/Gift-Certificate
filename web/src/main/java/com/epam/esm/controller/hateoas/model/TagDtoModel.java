package com.epam.esm.controller.hateoas.model;

import com.epam.esm.dto.impl.TagDto;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Relation(value = "tag", collectionRelation = "tags")
public class TagDtoModel extends RepresentationModel<TagDtoModel> {
    @Getter
    private final Long id;
    @Getter
    private final String name;

    public TagDtoModel(TagDto tagDto) {
        this.id = tagDto.getId();
        this.name = tagDto.getName();
    }
}
