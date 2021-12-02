package com.epam.esm.controller.hateoas.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.hateoas.model.TagDtoModel;
import com.epam.esm.dto.impl.TagDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class that sets a links to our TagDtoModel
 *
 * @author Nikita Kosikov
 * @version 1.0
 * @see org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
 */
public class TagDtoAssembler extends RepresentationModelAssemblerSupport<TagDto, TagDtoModel> {

    public TagDtoAssembler() {
        super(TagController.class, TagDtoModel.class);
    }

    @Override
    protected TagDtoModel instantiateModel(TagDto tagDto) {
        return new TagDtoModel(tagDto);
    }

    /**
     * Sets a links to our TagDtoModel.
     *
     * @param tagDto the object in which will be added links.
     * @return the TagDtoModel this contains links.
     */
    @Override
    public TagDtoModel toModel(TagDto tagDto) {
        TagDtoModel tagDtoModel = createModelWithId(tagDto.getId(), tagDto);
        tagDtoModel.add(linkTo(methodOn(TagController.class).save(tagDto)).withRel("save"));
        tagDtoModel.add(linkTo(methodOn(TagController.class).putUpdate(tagDto.getId(), tagDto)).withRel("put-update"));
        tagDtoModel.add(linkTo(methodOn(TagController.class).patchUpdate(tagDto.getId(), tagDto)).withRel("patch-update"));
        tagDtoModel.add(linkTo(methodOn(TagController.class).delete(tagDto.getId())).withRel("delete"));
        return tagDtoModel;
    }
}
