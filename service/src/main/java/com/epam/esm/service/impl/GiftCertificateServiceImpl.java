package com.epam.esm.service.impl;

import com.epam.esm.dto.impl.TagDto;
import com.epam.esm.specification.model.SearchByParameters;
import com.epam.esm.dto.impl.GiftCertificateDto;
import com.epam.esm.dto.impl.SearchByParametersDto;
import com.epam.esm.exception.CustomResourceNotFoundException;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.specification.SearchByParametersSpecification;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.util.MessageKey;
import com.epam.esm.validator.GiftCertificateValidator;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Class is implementation of interface {@link GiftCertificateService} and
 * intended to work with gift certificate
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final String GIFT_CERTIFICATE_COLUMN_NAME_DB="name";
    private static final String GIFT_CERTIFICATE_COLUMN_DESCRIPTION_DB="description";

    private final GiftCertificateRepository giftCertificateRepository;
    private final ModelMapper modelMapper;
    private final GiftCertificateValidator giftCertificateValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, ModelMapper modelMapper, GiftCertificateValidator giftCertificateValidator) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.modelMapper = modelMapper;
        this.giftCertificateValidator = giftCertificateValidator;
    }


    @Override
    public List<GiftCertificateDto> findAll(Pageable pageable, SearchByParametersDto searchByParametersDto) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), searchByParametersDto.getSort());
        SearchByParameters searchByParameters = modelMapper.map(searchByParametersDto, SearchByParameters.class);
        Page<GiftCertificate> giftCertificates = giftCertificateRepository.findAll(new SearchByParametersSpecification<>(searchByParameters), pageable);
        return modelMapper.map(giftCertificates.getContent(), new TypeToken<List<GiftCertificateDto>>() {}.getType());
    }

    @Override
    public GiftCertificateDto findById(Long id) throws CustomResourceNotFoundException{
        giftCertificateValidator.validateId(id);
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.findById(id);
        return giftCertificateOptional.map(giftCertificate -> modelMapper.map(giftCertificate, GiftCertificateDto.class))
                .orElseThrow(() -> new CustomResourceNotFoundException("no gift certificate by id",
                        MessageKey.GIFT_CERTIFICATE_NOT_FOUND_BY_ID, String.valueOf(id), ErrorCode.GIFT_CERTIFICATE.getCode()));
    }

    @Override
    public GiftCertificateDto save(GiftCertificateDto giftCertificateDto) throws IncorrectParameterValueException {
        giftCertificateValidator.validate(giftCertificateDto);
        GiftCertificate giftCertificate = modelMapper.map(giftCertificateDto, GiftCertificate.class);
        giftCertificate = giftCertificateRepository.save(giftCertificate);
        return modelMapper.map(giftCertificate, GiftCertificateDto.class);
    }

    @Override
    public GiftCertificateDto putUpdate(GiftCertificateDto giftCertificateDto, Long id) throws IncorrectParameterValueException{
        giftCertificateValidator.validateId(id);
        giftCertificateValidator.validate(giftCertificateDto);
        GiftCertificate giftCertificate = modelMapper.map(giftCertificateDto, GiftCertificate.class);
        giftCertificate.setId(id);
        giftCertificate = giftCertificateRepository.save(giftCertificate);
        return modelMapper.map(giftCertificate, GiftCertificateDto.class);
    }

    @Override
    public GiftCertificateDto patchUpdate(GiftCertificateDto newGiftCertificateDto, Long id)
            throws IncorrectParameterValueException, CustomResourceNotFoundException{
        GiftCertificateDto foundGiftCertificateDto = findById(id);
        updateFields(foundGiftCertificateDto, newGiftCertificateDto);
        giftCertificateValidator.validate(foundGiftCertificateDto);
        GiftCertificate giftCertificate = modelMapper.map(foundGiftCertificateDto, GiftCertificate.class);
        giftCertificate = giftCertificateRepository.save(giftCertificate);
        return modelMapper.map(giftCertificate, GiftCertificateDto.class);
    }

    @Override
    public void delete(Long id) throws CustomResourceNotFoundException{
        giftCertificateValidator.validateId(id);
        GiftCertificateDto foundGiftCertificateDto = findById(id);
        GiftCertificate giftCertificate = modelMapper.map(foundGiftCertificateDto, GiftCertificate.class);
        giftCertificateRepository.delete(giftCertificate);
    }

    /**
     * Adding fields to foundGiftCertificateDto from newGiftCertificateDto.
     *
     * @param foundGiftCertificateDto the gift certificate from database.
     * @param newGiftCertificateDto the new gift certificate from client.
     * @throws IncorrectParameterValueException if newGiftCertificateDto has all fields equal NULL.
     */
    private void updateFields(GiftCertificateDto foundGiftCertificateDto, GiftCertificateDto newGiftCertificateDto){
        int fieldCounter = 0;
        if (Objects.nonNull(newGiftCertificateDto.getName())) {
            foundGiftCertificateDto.setName(newGiftCertificateDto.getName());
            fieldCounter++;
        }
        if (Objects.nonNull(newGiftCertificateDto.getDescription())) {
            foundGiftCertificateDto.setDescription(newGiftCertificateDto.getDescription());
            fieldCounter++;
        }
        if (Objects.nonNull(newGiftCertificateDto.getPrice())) {
            foundGiftCertificateDto.setPrice(newGiftCertificateDto.getPrice());
            fieldCounter++;
        }
        if (Objects.nonNull(newGiftCertificateDto.getDuration())) {
            foundGiftCertificateDto.setDuration(newGiftCertificateDto.getDuration());
            fieldCounter++;
        }
        if (fieldCounter == 0) {
            Map<String, String> incorrectParameter = new HashMap<>();
            incorrectParameter.put(MessageKey.NO_FIELDS_TO_UPDATE, StringUtils.EMPTY);
            throw new IncorrectParameterValueException("no fields to update", incorrectParameter,
                    ErrorCode.GIFT_CERTIFICATE.getCode());
        }
    }

}
