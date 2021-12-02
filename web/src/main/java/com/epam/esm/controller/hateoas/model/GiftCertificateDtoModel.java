package com.epam.esm.controller.hateoas.model;

import com.epam.esm.controller.hateoas.assembler.TagDtoAssembler;
import com.epam.esm.dto.impl.GiftCertificateDto;
import lombok.Getter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Relation(value = "gift-certificate", collectionRelation = "gift-certificates")
public class GiftCertificateDtoModel extends RepresentationModel<GiftCertificateDtoModel> {

    private static final TagDtoAssembler tagDtoAssembler = new TagDtoAssembler();

    @Getter
    private final Long id;
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final BigDecimal price;
    @Getter
    private final int duration;
    @Getter
    private final LocalDateTime createDate;
    @Getter
    private final LocalDateTime lastUpdateDate;
    @Getter
    private final CollectionModel<TagDtoModel> tags;

    public GiftCertificateDtoModel(GiftCertificateDto giftCertificateDto) {
        this.id = giftCertificateDto.getId();
        this.name = giftCertificateDto.getName();
        this.description = giftCertificateDto.getDescription();
        this.price = giftCertificateDto.getPrice();
        this.duration = giftCertificateDto.getDuration();
        this.createDate = giftCertificateDto.getCreateDate();
        this.lastUpdateDate = giftCertificateDto.getLastUpdateDate();
        this.tags = tagDtoAssembler.toCollectionModel(giftCertificateDto.getTags());
    }
}
