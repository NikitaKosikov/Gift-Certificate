package com.epam.esm.service.impl;

import com.epam.esm.config.ServiceConfiguration;
import com.epam.esm.dto.impl.*;
import com.epam.esm.entity.*;
import com.epam.esm.exception.CustomResourceNotFoundException;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.specification.SearchByParametersSpecification;
import com.epam.esm.specification.model.SearchByParameters;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ServiceConfiguration.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;
    @Mock
    private TagValidator tagValidator;
    private TagService tagService;
    @Mock
    private ModelMapper modelMapper;

    private static Tag tag1;
    private static Tag tag2;
    private static Tag tag3;
    private static Tag tag4;
    private static Tag tag5;
    private static TagDto tagDto1;
    private static TagDto tagDto2;
    private static TagDto tagDto3;
    private static TagDto tagDto4;
    private static TagDto tagDto5;
    private static TagDto tagDto6;
    private static TagDto tagDto7;
    private static TagDto tagDto8;
    private static TagDto tagDto9;
    private static Pageable pageable;
    private static Page<Tag> tagPage;
    private static List<Tag> tags;
    private static List<TagDto> tagsDto;
    private static SearchByParametersDto searchByParametersDto;
    private static SearchByParameters searchByParameters;


    @BeforeEach
    public void initUseCase(){
        tagService = new TagServiceImpl(tagRepository, modelMapper, tagValidator);
    }

    @BeforeAll
    public static void setUp(){
        tag1 = new Tag(1L, "user1");
        tag2 = new Tag(null, "user2");
        tag3 = new Tag(2L, "user3");
        tag4 = new Tag(2L, "user3");
        tag5 = new Tag(2L, "user5");
        tagDto1 = new TagDto(1L, "user1");
        tagDto2 = new TagDto(null, "user1");
        tagDto3 = new TagDto(2L, "user1");
        tagDto4 = new TagDto(2L, "user1");
        tagDto5 = new TagDto(2L, "user1");
        tagDto6 = new TagDto(2L, "user5");
        tagDto7 = new TagDto(null, "user5");
        tagDto8 = new TagDto(null, "user1");
        tagDto9 = new TagDto(null, null);
        pageable = PageRequest.of(1,5, Sort.unsorted());
        tags = List.of(tag1, tag2);
        tagsDto = List.of(tagDto1, tagDto2);
        searchByParametersDto = new SearchByParametersDto(new HashMap<>(), new ArrayList<>(), Sort.unsorted());
        searchByParameters = new SearchByParameters();
        tagPage = new PageImpl<>(List.of(tag1, tag2));
    }

    @AfterAll
    public static void tearDown(){
        tag1 = null;
        tag2 = null;
        tag3 = null;
        tag4 = null;
        tag5 = null;
        tagDto1 = null;
        tagDto2 = null;
        tagDto3 = null;
        tagDto4 = null;
        tagDto5 = null;
        tagDto6 = null;
        tagDto7 = null;
        tagDto8 = null;
        tagDto9 = null;
        pageable = null;
        tags = null;
        tagsDto = null;
    }

    @Test
    void findAll() {
        Type type = new TypeToken<List<TagDto>>() {}.getType();
        when(modelMapper.map(searchByParametersDto, SearchByParameters.class)).thenReturn(searchByParameters);
        when(tagRepository.findAll(isA(SearchByParametersSpecification.class), isA(Pageable.class))).thenReturn(tagPage);
        when(modelMapper.map(tags, type)).thenReturn(tagsDto);

        List<TagDto> actualList = tagService.findAll(pageable, searchByParametersDto);

        assertEquals(actualList, tagsDto);
    }

    @Test
    void findByIdPositiveResult() {
        final Long id = 1L;
        doNothing().when(tagValidator).validateId(anyLong());
        when(tagRepository.findById(id)).thenReturn(Optional.of(tag1));
        when(modelMapper.map(tag1, TagDto.class)).thenReturn(tagDto1);

        TagDto actualTagDto = tagService.findById(id);

        assertEquals(tagDto1, actualTagDto);
    }

    @Test
    void findByIdNotFoundUserException() {
        final Long id = 100000L;
        doNothing().when(tagValidator).validateId(anyLong());
        when(tagRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class, () -> tagService.findById(id));
    }

    @Test
    void findByIdIncorrectIdException() {
        final Long id = -200L;
        doThrow(new IncorrectParameterValueException()).when(tagValidator).validateId(id);
        assertThrows(IncorrectParameterValueException.class, () -> tagService.findById(id));
    }

    @Test
    void savePositiveTest() {
        doNothing().when(tagValidator).validate(tagDto2);
        when(modelMapper.map(tagDto2, Tag.class)).thenReturn(tag2);
        when(tagRepository.save(tag2)).thenReturn(tag3);
        when(modelMapper.map(tag3, TagDto.class)).thenReturn(tagDto3);

        TagDto actualTagDto = tagService.save(tagDto2);

        assertEquals(tagDto3, actualTagDto);
    }

    @Test
    void TagValidatorInvalidUsername() {
        TagDto tagDtoNameIsNull = new TagDto(1L, null);
        TagDto tagDtoNameIsBlank = new TagDto(2L, "");
        TagDto tagDtoNameIsNotMatcherPattern = new TagDto(3L, new String(new char[101]));

        doThrow(new IncorrectParameterValueException()).when(tagValidator).validate(tagDtoNameIsNull);
        doThrow(new IncorrectParameterValueException()).when(tagValidator).validate(tagDtoNameIsBlank);
        doThrow(new IncorrectParameterValueException()).when(tagValidator).validate(tagDtoNameIsNotMatcherPattern);

        assertThrows(IncorrectParameterValueException.class, () -> tagService.save(tagDtoNameIsNull));
        assertThrows(IncorrectParameterValueException.class, () -> tagService.save(tagDtoNameIsBlank));
        assertThrows(IncorrectParameterValueException.class, () -> tagService.save(tagDtoNameIsNotMatcherPattern));
    }

    @Test
    void putUpdatePositiveTest() {
        final Long id = 2L;
        doNothing().when(tagValidator).validateId(anyLong());
        doNothing().when(tagValidator).validate(isA(TagDto.class));
        when(modelMapper.map(tagDto4, Tag.class)).thenReturn(tag4);
        when(modelMapper.map(tag4, TagDto.class)).thenReturn(tagDto5);
        when(tagRepository.save(tag4)).thenReturn(tag4);

        TagDto actualGiftCertificateDto = tagService.putUpdate(tagDto4, id);

        assertEquals(tagDto5, actualGiftCertificateDto);
    }

    @Test
    void patchUpdatePositiveTest() {
        final Long id = 2L;
        doNothing().when(tagValidator).validateId(anyLong());
        doNothing().when(tagValidator).validate(isA(TagDto.class));
        when(tagRepository.findById(id)).thenReturn(Optional.of(tag5));
        when(modelMapper.map(tag5, TagDto.class)).thenReturn(tagDto6);
        when(modelMapper.map(tagDto6, Tag.class)).thenReturn(tag5);
        when(tagRepository.save(tag5)).thenReturn(tag5);

        TagDto actualTagDto = tagService.patchUpdate(tagDto7, id);

        assertEquals(tagDto6, actualTagDto);
    }

    @Test
    void patchUpdateEntitiesNotChangedThrowIncorrectParametersExceptionTest() {
        final Long id = 2L;

        doNothing().when(tagValidator).validateId(anyLong());
        when(tagRepository.findById(id)).thenReturn(Optional.of(tag5));
        when(modelMapper.map(tag5, TagDto.class)).thenReturn(tagDto6);

        assertThrows(IncorrectParameterValueException.class, () -> tagService.patchUpdate(tagDto9, id));
    }


    @Test
    void deletePositiveTest() {
        final Long id = 2L;
        doNothing().when(tagValidator).validateId(anyLong());
        when(tagRepository.findById(id)).thenReturn(Optional.of(tag3));
        when(modelMapper.map(tag3, TagDto.class)).thenReturn(tagDto3);
        when(modelMapper.map(tagDto3, Tag.class)).thenReturn(tag3);

        doNothing().when(tagRepository).delete(tag3);

        assertDoesNotThrow(() -> tagService.delete(id));
    }

    @Test
    void deleteTagThrowIncorrectParameterValueExceptionTest() {
        final long id = -5;
        doThrow(new IncorrectParameterValueException()).when(tagValidator).validateId(anyLong());
        assertThrows(IncorrectParameterValueException.class, () -> tagService.delete(id));
    }

    @Test
    public void deleteTagThrowResourceNotFoundExceptionTest() {
        final Long id = 10L;
        doNothing().when(tagValidator).validateId(anyLong());
        when(tagRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class, () -> tagService.delete(id));
    }
}