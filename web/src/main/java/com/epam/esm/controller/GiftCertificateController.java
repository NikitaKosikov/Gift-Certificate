package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.model.GiftCertificateDtoModel;
import com.epam.esm.controller.hateoas.link.CustomLink;
import com.epam.esm.controller.hateoas.model.TagDtoModel;
import com.epam.esm.converter.ParametersToDtoConverter;
import com.epam.esm.dto.impl.GiftCertificateDto;
import com.epam.esm.dto.impl.SearchByParametersDto;
import com.epam.esm.dto.impl.TagDto;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Class is an endpoint for user authentication and registration
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@RestController
@RequestMapping("/gift-certificates")
@PreAuthorize("hasRole('ADMIN')")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final CustomLink<GiftCertificateDtoModel, GiftCertificateDto> giftCertificateDtoCustomLink;
    private final CustomLink<TagDtoModel, TagDto> tagDtoCustomLink;
    private final ParametersToDtoConverter parametersToDtoConverter;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, CustomLink<GiftCertificateDtoModel, GiftCertificateDto> giftCertificateDtoCustomLink, CustomLink<TagDtoModel, TagDto> tagDtoCustomLink, ParametersToDtoConverter parametersToDtoConverter){
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateDtoCustomLink = giftCertificateDtoCustomLink;
        this.tagDtoCustomLink = tagDtoCustomLink;
        this.parametersToDtoConverter = parametersToDtoConverter;
    }

    /**
     * Find gift certificates by parameters, processes GET requests at
     * /gift-certificates
     *
     * @param pageable the contains information about the data selection.
     * @param parameters the gift certificate search parameters.
     * @return the responseEntity object with contains HTTP status (OK)
     * and found gift certificates as CollectionModel<GiftCertificateDtoModel>.
     */
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<CollectionModel<GiftCertificateDtoModel>> findAll(Pageable pageable, @RequestParam(required = false) Map<String, String> parameters){
        SearchByParametersDto searchByParametersDto = parametersToDtoConverter.convert(parameters);
        List<GiftCertificateDto> giftCertificates = giftCertificateService.findAll(pageable, searchByParametersDto);
        CollectionModel<GiftCertificateDtoModel> giftCertificateDtoCollectionModel = giftCertificateDtoCustomLink.toLink(giftCertificates);
        return new ResponseEntity<>(giftCertificateDtoCollectionModel, HttpStatus.OK);
    }


    /**
     * Find gift certificate by id, processes GET requests at /gift-certificates/{id}.
     *
     * @param id the gift certificate id which will be found.
     * @return the responseEntity object with contains HTTP status (OK)
     * and the found gift certificate as GiftCertificateDtoModel.
     */
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<GiftCertificateDtoModel> findById(@PathVariable Long id){
        GiftCertificateDto giftCertificate = giftCertificateService.findById(id);
        GiftCertificateDtoModel giftCertificateDtoModel = giftCertificateDtoCustomLink.toLink(giftCertificate);
        return new ResponseEntity<>(giftCertificateDtoModel, HttpStatus.OK);
    }

    /**
     * Create new gift certificate, processes POST requests at /gift-certificates
     *
     * @param giftCertificateDto the new gift certificate which will be created.
     * @return the responseEntity object with contains HTTP status (CREATED)
     * and the saved gift certificate as GiftCertificateDtoModel.
     */
    @PostMapping
    public ResponseEntity<GiftCertificateDtoModel> save(@RequestBody GiftCertificateDto giftCertificateDto){
        GiftCertificateDto giftCertificate = giftCertificateService.save(giftCertificateDto);
        GiftCertificateDtoModel giftCertificateDtoModel = giftCertificateDtoCustomLink.toLink(giftCertificate);
        return new ResponseEntity<>(giftCertificateDtoModel, HttpStatus.CREATED);
    }

    /**
     * Full gift certificate update, processes PUT requests at /gift-certificates/{id}
     *
     * @param id the gift certificate id which will be updated.
     * @param giftCertificateDto the gift certificate dto with updated fields.
     * @return the responseEntity object with contains HTTP status (OK)
     * and the updated gift certificate as GiftCertificateDtoModel.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GiftCertificateDtoModel> putUpdate(@PathVariable Long id, @RequestBody GiftCertificateDto giftCertificateDto){
        GiftCertificateDto giftCertificate = giftCertificateService.putUpdate(giftCertificateDto, id);
        GiftCertificateDtoModel giftCertificateDtoModel = giftCertificateDtoCustomLink.toLink(giftCertificate);
        return new ResponseEntity<>(giftCertificateDtoModel, HttpStatus.OK);
    }

    /**
     * Partial gift certificate update, processes PATCH requests at /gift-certificates/{id}
     *
     * @param id the gift certificate id which will be updated.
     * @param giftCertificateDto the gift certificate dto with updated fields.
     * @return the responseEntity object with contains HTTP status (OK)
     * and the updated gift certificate as GiftCertificateDtoModel.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificateDtoModel> patchUpdate(@PathVariable Long id, @RequestBody GiftCertificateDto giftCertificateDto){
        GiftCertificateDto giftCertificate = giftCertificateService.patchUpdate(giftCertificateDto, id);
        GiftCertificateDtoModel giftCertificateDtoModel = giftCertificateDtoCustomLink.toLink(giftCertificate);
        return new ResponseEntity<>(giftCertificateDtoModel, HttpStatus.OK);
    }

    /**
     * Delete gift certificate by id, processes DELETE requests at
     * /gift-certificates/{id}.
     *
     * @param id the gift certificate id which will be deleted.
     * @return the responseEntity object with contains only HTTP status (NO_CONTENT).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<GiftCertificateDtoModel> delete(@PathVariable Long id){
        giftCertificateService.delete(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
