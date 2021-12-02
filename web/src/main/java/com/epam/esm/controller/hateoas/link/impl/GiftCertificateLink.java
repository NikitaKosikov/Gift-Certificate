package com.epam.esm.controller.hateoas.link.impl;

import com.epam.esm.controller.hateoas.assembler.GiftCertificateDtoAssembler;
import com.epam.esm.controller.hateoas.model.GiftCertificateDtoModel;
import com.epam.esm.controller.hateoas.link.CustomLink;
import com.epam.esm.dto.impl.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Class is implementation of interface {@link CustomLink} and
 * intended to work with gift certificate links
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Component
public class GiftCertificateLink implements CustomLink<GiftCertificateDtoModel, GiftCertificateDto> {

    @Override
    public GiftCertificateDtoModel toLink(GiftCertificateDto entity) {
        return new GiftCertificateDtoAssembler().toModel(entity);
    }

    @Override
    public CollectionModel<GiftCertificateDtoModel> toLink(List<GiftCertificateDto> entities) {
        if (entities!=null && !entities.isEmpty()){
            return new GiftCertificateDtoAssembler().toCollectionModel(entities);
        }
        return CollectionModel.empty();
    }
}
