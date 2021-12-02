package com.epam.esm.service;

import com.epam.esm.dto.impl.GiftCertificateDto;
import com.epam.esm.dto.impl.SearchByParametersDto;
import com.epam.esm.dto.impl.TagDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
public interface GiftCertificateService {

    /**
     * Looking for a set of gift certificates as dto.
     *
     * @param pageable the contains information about the data selection.
     * @param searchByParametersDto the object that contains sorting and filter by fields.
     * @return list of found gift certificates as dto or List#empty() if none found.
     */
    List<GiftCertificateDto> findAll(Pageable pageable, SearchByParametersDto searchByParametersDto);

    /**
     * Looking for the gift certificate as dto by specific id.
     *
     * @param id the id of gift certificate.
     * @return the found gift certificate as dto or throw CustomResourceNotFoundException if none found.
     */
    GiftCertificateDto findById(Long id);

    /**
     * Create giftCertificate.
     *
     * @param giftCertificateDto the gift certificate dto by which gift certificate will be created.
     * @return the created gift certificate as dto.
     */
    GiftCertificateDto save(GiftCertificateDto giftCertificateDto);

    /**
     * Full gift certificate update.
     *
     * @param giftCertificateDto the gift certificate as dto that will replace the old one.
     * @param id the id old gift certificate that will be replacement.
     * @return the updated gift certificate as dto.
     */
    GiftCertificateDto putUpdate(GiftCertificateDto giftCertificateDto, Long id);

    /**
     * Partial gift certificate update.
     *
     * @param newGiftCertificateDto the gift certificate as dto that will replace the old one.
     * @param id the id old gift certificate that will be replacement.
     * @return the updated gift certificate as dto.
     */
    GiftCertificateDto patchUpdate(GiftCertificateDto newGiftCertificateDto, Long id);

    /**
     * Delete the gift certificate by specific id.
     *
     * @param id the id of the gift certificate.
     */
    void delete(Long id);
}
