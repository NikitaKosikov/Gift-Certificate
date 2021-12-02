package com.epam.esm.controller.hateoas.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.hateoas.model.GiftCertificateDtoModel;
import com.epam.esm.dto.impl.GiftCertificateDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class that sets a links to our GiftCertificateDtoModel
 *
 * @author Nikita Kosikov
 * @version 1.0
 * @see org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
 */
public class GiftCertificateDtoAssembler extends RepresentationModelAssemblerSupport<GiftCertificateDto, GiftCertificateDtoModel> {

    public GiftCertificateDtoAssembler() {
        super(GiftCertificateController.class, GiftCertificateDtoModel.class);
    }

    @Override
    protected GiftCertificateDtoModel instantiateModel(GiftCertificateDto giftCertificateDto) {
        return new GiftCertificateDtoModel(giftCertificateDto);
    }

    /**
     * Sets a links to our GiftCertificateDtoModel.
     *
     * @param giftCertificateDto the object in which will be added links.
     * @return the GiftCertificateDtoModel this contains links.
     */
    @Override
    public GiftCertificateDtoModel toModel(GiftCertificateDto giftCertificateDto) {
        GiftCertificateDtoModel giftCertificateDtoModel = createModelWithId(giftCertificateDto.getId(), giftCertificateDto);
        giftCertificateDtoModel.add(linkTo(methodOn(GiftCertificateController.class).save(giftCertificateDto)).withRel("save"));
        giftCertificateDtoModel.add(linkTo(methodOn(GiftCertificateController.class).putUpdate(giftCertificateDto.getId(), giftCertificateDto)).withRel("put-update"));
        giftCertificateDtoModel.add(linkTo(methodOn(GiftCertificateController.class).patchUpdate(giftCertificateDto.getId(), giftCertificateDto)).withRel("patch-update"));
        giftCertificateDtoModel.add(linkTo(methodOn(GiftCertificateController.class).delete(giftCertificateDto.getId())).withRel("delete"));
        return giftCertificateDtoModel;
    }
}
