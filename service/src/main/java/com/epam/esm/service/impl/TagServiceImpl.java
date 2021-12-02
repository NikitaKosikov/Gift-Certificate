package com.epam.esm.service.impl;

import com.epam.esm.specification.model.SearchByParameters;
import com.epam.esm.dto.impl.SearchByParametersDto;
import com.epam.esm.dto.impl.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CustomResourceNotFoundException;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.specification.SearchByParametersSpecification;
import com.epam.esm.service.TagService;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.MessageKey;
import com.epam.esm.validator.TagValidator;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Class is implementation of interface {@link TagService} and intended to work
 * with gift certificate
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Service
public class TagServiceImpl implements TagService {

    private static final String TAG_COLUMN_NAME_DB="name";

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final TagValidator tagValidator;

    public TagServiceImpl(TagRepository tagRepository, ModelMapper modelMapper, TagValidator tagValidator) {
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
        this.tagValidator = tagValidator;
    }

    @Override
    public List<TagDto> findAll(Pageable pageable, SearchByParametersDto searchByParametersDto) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), searchByParametersDto.getSort());
        SearchByParameters searchByParameters = modelMapper.map(searchByParametersDto, SearchByParameters.class);
        Page<Tag> tags = tagRepository.findAll(new SearchByParametersSpecification<>(searchByParameters), pageable);
        return modelMapper.map(tags.getContent(), new TypeToken<List<TagDto>>() {}.getType());
    }

    @Override
    public TagDto findById(Long id) throws CustomResourceNotFoundException {
        tagValidator.validateId(id);
        Optional<Tag> tagOptional = tagRepository.findById(id);
        return tagOptional.map(tag -> modelMapper.map(tag, TagDto.class))
                .orElseThrow(() -> new CustomResourceNotFoundException("no tag by id",
                        MessageKey.ORDER_NOT_FOUND_BY_ID, String.valueOf(id), ErrorCode.TAG.getCode()));
    }

    @Override
    public TagDto save(TagDto tagDto) throws IncorrectParameterValueException{
        tagValidator.validate(tagDto);
        Tag tag = modelMapper.map(tagDto, Tag.class);
        tag = tagRepository.save(tag);
        return modelMapper.map(tag, TagDto.class);
    }

    @Override
    public TagDto putUpdate(TagDto tagDto, Long id) throws IncorrectParameterValueException{
        tagValidator.validateId(id);
        tagValidator.validate(tagDto);
        Tag tag = modelMapper.map(tagDto, Tag.class);
        tag.setId(id);
        tag = tagRepository.save(tag);
        return modelMapper.map(tag, TagDto.class);
    }

    @Override
    public TagDto patchUpdate(TagDto newTagDto, Long id) throws
            IncorrectParameterValueException, CustomResourceNotFoundException {
        TagDto foundTagDto = findById(id);
        updateFields(foundTagDto, newTagDto);
        tagValidator.validate(foundTagDto);
        Tag tag = modelMapper.map(foundTagDto, Tag.class);
        tag = tagRepository.save(tag);
        return modelMapper.map(tag, TagDto.class);
    }

    @Override
    public void delete(Long id) {
        tagValidator.validateId(id);
        TagDto tagDto = findById(id);
        Tag tag = modelMapper.map(tagDto, Tag.class);
        tagRepository.delete(tag);
    }

    /**
     * Adding fields to foundTagDto from newTagDto.
     *
     * @param foundTagDto the tag from database.
     * @param newTagDto the new tag from client.
     * @throws IncorrectParameterValueException if newTagDto has all fields equal NULL.
     */
    private void updateFields(TagDto foundTagDto, TagDto newTagDto){
        int fieldUpdated = 0;
        if (Objects.nonNull(newTagDto.getName())) {
            foundTagDto.setName(newTagDto.getName());
            fieldUpdated++;
        }
        if (fieldUpdated == 0) {
            Map<String, String> incorrectParameter = new HashMap<>();
            incorrectParameter.put(MessageKey.NO_FIELDS_TO_UPDATE, StringUtils.EMPTY);
            throw new IncorrectParameterValueException("no fields to update", incorrectParameter,
                    ErrorCode.TAG.getCode());
        }
    }
}
