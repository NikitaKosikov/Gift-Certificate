package com.epam.esm.service.impl;

import com.epam.esm.config.ServiceConfiguration;
import com.epam.esm.dto.impl.*;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.CustomResourceNotFoundException;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.specification.SearchByParametersSpecification;
import com.epam.esm.specification.model.SearchByParameters;
import com.epam.esm.validator.GiftCertificateValidator;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ServiceConfiguration.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private GiftCertificateValidator giftCertificateValidator;
    private GiftCertificateService giftCertificateService;
    @Mock
    private ModelMapper modelMapper;
    private static GiftCertificate giftCertificate1;
    private static GiftCertificate giftCertificate2;
    private static GiftCertificate giftCertificate3;
    private static GiftCertificate giftCertificate4;
    private static GiftCertificate giftCertificate5;
    private static GiftCertificateDto giftCertificateDto1;
    private static GiftCertificateDto giftCertificateDto2;
    private static GiftCertificateDto giftCertificateDto3;
    private static GiftCertificateDto giftCertificateDto4;
    private static GiftCertificateDto giftCertificateDto5;
    private static GiftCertificateDto giftCertificateDto6;
    private static GiftCertificateDto giftCertificateDto7;
    private static GiftCertificateDto giftCertificateDto8;
    private static GiftCertificateDto giftCertificateDto9;
    private static Pageable pageable;
    private static Page<GiftCertificate> giftCertificatePage;
    private static List<GiftCertificate> giftCertificates;
    private static List<GiftCertificateDto> giftCertificatesDto;
    private static SearchByParametersDto searchByParametersDto;
    private static SearchByParameters searchByParameters;

    @BeforeEach
    public void initUseCase(){
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository, modelMapper, giftCertificateValidator);
    }

    @BeforeAll
    public static void setUp(){
        giftCertificate1 = new GiftCertificate(1L, "Cinema", "Best cinema in the city", new BigDecimal(100), 5,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        giftCertificate2 = new GiftCertificate(null, "Travel to German", "You will like it", new BigDecimal(100), 10,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        giftCertificate3 = new GiftCertificate(2L, "Travel to German", "You will like it", new BigDecimal(100), 10,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        giftCertificate4 = new GiftCertificate(2L, "Update", "UpdateCinema", new BigDecimal(100), 15,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        giftCertificate5 = new GiftCertificate(2L, "Update-Patch", "UpdateCinemaPatch",  new BigDecimal(100), 15,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        giftCertificateDto1 = new GiftCertificateDto(1L, "Cinema", "Best cinema in the city", new BigDecimal(100), 5,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        giftCertificateDto2 = new GiftCertificateDto(null, "Cinema", "Best cinema in the city", new BigDecimal(100), 5,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        giftCertificateDto3 = new GiftCertificateDto(2L, "Cinema", "Cinema", new BigDecimal(100), 15,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        giftCertificateDto4 = new GiftCertificateDto(null, "Update", "UpdateCinema", new BigDecimal(100), 15,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        giftCertificateDto5 = new GiftCertificateDto(2L, "Update", "UpdateCinema", new BigDecimal(100), 15,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        giftCertificateDto6 = new GiftCertificateDto(2L, "Update-Patch", "UpdateCinemaPatch",  new BigDecimal(100), 15,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        giftCertificateDto7 = new GiftCertificateDto(null, "Update-Patch", "UpdateCinemaPatch", null, 15,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        giftCertificateDto8 = new GiftCertificateDto(null, "Update-Patch", "UpdateCinemaPatch", new BigDecimal(100), 15,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        giftCertificateDto9 = new GiftCertificateDto(null, null, null, null, null,
                null, null, Collections.emptyList());
        pageable = PageRequest.of(1,5, Sort.unsorted());
        giftCertificates = List.of(giftCertificate1, giftCertificate2);
        giftCertificatesDto= List.of(giftCertificateDto1, giftCertificateDto2);
        searchByParametersDto = new SearchByParametersDto(new HashMap<>(), new ArrayList<>(), Sort.unsorted());
        searchByParameters = new SearchByParameters();
        giftCertificatePage = new PageImpl<>(List.of(giftCertificate1, giftCertificate2));
    }

    @AfterAll
    public static void tearDown(){
        giftCertificate1 = null;
        giftCertificate2 = null;
        giftCertificate3 = null;
        giftCertificate4 = null;
        giftCertificateDto1 = null;
        giftCertificateDto2 = null;
        giftCertificateDto3 = null;
        giftCertificateDto4 = null;
        giftCertificateDto5 = null;
        giftCertificateDto6 = null;
        giftCertificateDto7 = null;
        giftCertificateDto8 = null;
        giftCertificateDto9 = null;
        pageable = null;
        giftCertificates = null;
        giftCertificatesDto = null;
    }

    @Test
    void findAllPositive() {
        Type type = new TypeToken<List<GiftCertificateDto>>() {}.getType();
        when(modelMapper.map(searchByParametersDto, SearchByParameters.class)).thenReturn(searchByParameters);
        when(giftCertificateRepository.findAll(isA(SearchByParametersSpecification.class), isA(Pageable.class))).thenReturn(giftCertificatePage);
        when(modelMapper.map(giftCertificates, type)).thenReturn(giftCertificatesDto);

        List<GiftCertificateDto> actualList = giftCertificateService.findAll(pageable, searchByParametersDto);

        assertEquals(actualList, giftCertificatesDto);
    }

    @Test
    void findByIdPositiveResult() {
        final Long id = 1L;
        doNothing().when(giftCertificateValidator).validateId(anyLong());
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate1));
        when(modelMapper.map(giftCertificate1, GiftCertificateDto.class)).thenReturn(giftCertificateDto1);

        GiftCertificateDto actualGiftCertificateDto = giftCertificateService.findById(id);

        assertEquals(giftCertificateDto1, actualGiftCertificateDto);
    }

    @Test
    void findByIdNotFoundUserException() {
        final Long id = 100000L;
        doNothing().when(giftCertificateValidator).validateId(anyLong());
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class, () -> giftCertificateService.findById(id));
    }

    @Test
    void findByIdIncorrectIdException() {
        final Long id = -200L;
        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validateId(id);
        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.findById(id));
    }

    @Test
    void savePositiveTest() {
        doNothing().when(giftCertificateValidator).validate(giftCertificateDto2);
        when(modelMapper.map(giftCertificateDto2, GiftCertificate.class)).thenReturn(giftCertificate2);
        when(giftCertificateRepository.save(giftCertificate2)).thenReturn(giftCertificate3);
        when(modelMapper.map(giftCertificate3, GiftCertificateDto.class)).thenReturn(giftCertificateDto3);

        GiftCertificateDto actualGiftCertificateDto = giftCertificateService.save(giftCertificateDto2);

        assertEquals(giftCertificateDto3, actualGiftCertificateDto);
    }

    @Test
    void GiftCertificateValidatorInvalidGiftCertificateName() {

        String hundredAndOneCharactersName = new String(new char[101]);
        GiftCertificateDto giftCertificateDtoNameIsNull = new GiftCertificateDto(2L, null, "", new BigDecimal(100), -5,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        GiftCertificateDto giftCertificateDtoNameIsBlank = new GiftCertificateDto(2L, "", "", new BigDecimal(100), -5,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        GiftCertificateDto giftCertificateDtoNameMoreThanMaxLength =  new GiftCertificateDto(2L, hundredAndOneCharactersName, "", new BigDecimal(100), -5,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());

        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validate(giftCertificateDtoNameIsNull);
        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validate(giftCertificateDtoNameIsBlank);
        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validate(giftCertificateDtoNameMoreThanMaxLength);

        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.save(giftCertificateDtoNameIsNull));
        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.save(giftCertificateDtoNameIsBlank));
        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.save(giftCertificateDtoNameMoreThanMaxLength));
    }

    @Test
    void GiftCertificateValidatorInvalidGiftCertificateDescription() {
        String thousandAndOneCharactersDescription = new String(new char[1001]);
        GiftCertificateDto giftCertificateDtoDescriptionIsNull = new GiftCertificateDto(2L, "null", null, new BigDecimal(100), 5,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        GiftCertificateDto giftCertificateDtoDescriptionIsBlank = new GiftCertificateDto(2L, "dsad", "", new BigDecimal(100), 15,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        GiftCertificateDto giftCertificateDtoDescriptionMoreThanMaxLength =  new GiftCertificateDto(2L, "hundredAndMoreCharacters", thousandAndOneCharactersDescription, new BigDecimal(100), -5,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());

        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validate(giftCertificateDtoDescriptionIsNull);
        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validate(giftCertificateDtoDescriptionIsBlank);
        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validate(giftCertificateDtoDescriptionMoreThanMaxLength);

        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.save(giftCertificateDtoDescriptionIsNull));
        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.save(giftCertificateDtoDescriptionIsBlank));
        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.save(giftCertificateDtoDescriptionMoreThanMaxLength));
    }

    @Test
    void GiftCertificateValidatorInvalidGiftCertificatePrice() {
        GiftCertificateDto giftCertificateDtoPriceIsNull = new GiftCertificateDto(2L, "null", "sda", null, 10,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        GiftCertificateDto giftCertificateDtoLessThanTwo = new GiftCertificateDto(2L, "dsad", "", new BigDecimal("100.0"), 25,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        GiftCertificateDto giftCertificateDtoMoreThanTwo = new GiftCertificateDto(2L, "dsad", "", new BigDecimal("100.000"), 35,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        GiftCertificateDto giftCertificateDtoPriceLessThanRequiredPrice =  new GiftCertificateDto(2L, "hundredAndMoreCharacters", "thousandAndOneCharactersDescription", new BigDecimal("0.00000001"), 15,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        GiftCertificateDto giftCertificateDtoPriceMoreThanRequiredPrice =  new GiftCertificateDto(2L, "hundredAndMoreCharacters", "thousandAndOneCharactersDescription", new BigDecimal("12321321321321312"), 15,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());

        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validate(giftCertificateDtoPriceIsNull);
        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validate(giftCertificateDtoLessThanTwo);
        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validate(giftCertificateDtoMoreThanTwo);
        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validate(giftCertificateDtoPriceLessThanRequiredPrice);
        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validate(giftCertificateDtoPriceMoreThanRequiredPrice);

        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.save(giftCertificateDtoPriceIsNull));
        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.save(giftCertificateDtoLessThanTwo));
        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.save(giftCertificateDtoMoreThanTwo));
        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.save(giftCertificateDtoPriceLessThanRequiredPrice));
        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.save(giftCertificateDtoPriceMoreThanRequiredPrice));
    }

    @Test
    void GiftCertificateValidatorInvalidGiftCertificateDuration() {
        GiftCertificateDto giftCertificateDtoDurationIsNull = new GiftCertificateDto(2L, "null", "null", new BigDecimal(100), null,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        GiftCertificateDto giftCertificateDtoDurationLessThanMinLength = new GiftCertificateDto(2L, "dsad", "", new BigDecimal(100), -5,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());
        GiftCertificateDto giftCertificateDtoDurationMoreThanMaxLength =  new GiftCertificateDto(2L, "hundredAndMoreCharacters", "thousandAndOneCharactersDescription", new BigDecimal(100), 1001,
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Collections.emptyList());

        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validate(giftCertificateDtoDurationIsNull);
        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validate(giftCertificateDtoDurationLessThanMinLength);
        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validate(giftCertificateDtoDurationMoreThanMaxLength);

        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.save(giftCertificateDtoDurationIsNull));
        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.save(giftCertificateDtoDurationLessThanMinLength));
        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.save(giftCertificateDtoDurationMoreThanMaxLength));
    }


    @Test
    void putUpdatePositiveTest() {
        final Long id = 2L;
        doNothing().when(giftCertificateValidator).validateId(anyLong());
        doNothing().when(giftCertificateValidator).validate(isA(GiftCertificateDto.class));
        when(modelMapper.map(giftCertificateDto4, GiftCertificate.class)).thenReturn(giftCertificate4);
        when(modelMapper.map(giftCertificate4, GiftCertificateDto.class)).thenReturn(giftCertificateDto5);
        when(giftCertificateRepository.save(giftCertificate4)).thenReturn(giftCertificate4);

        GiftCertificateDto actualGiftCertificateDto = giftCertificateService.putUpdate(giftCertificateDto4, id);

        assertEquals(giftCertificateDto5, actualGiftCertificateDto);
    }

    @Test
    void patchUpdatePositiveTest() {
        final Long id = 2L;

        doNothing().when(giftCertificateValidator).validateId(anyLong());
        doNothing().when(giftCertificateValidator).validate(isA(GiftCertificateDto.class));
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate5));
        when(modelMapper.map(giftCertificate5, GiftCertificateDto.class)).thenReturn(giftCertificateDto6);
        when(modelMapper.map(giftCertificateDto6, GiftCertificate.class)).thenReturn(giftCertificate5);
        when(giftCertificateRepository.save(giftCertificate5)).thenReturn(giftCertificate5);

        GiftCertificateDto actualGiftCertificateDto = giftCertificateService.patchUpdate(giftCertificateDto7, id);

        assertEquals(giftCertificateDto6, actualGiftCertificateDto);
    }

    @Test
    void patchUpdateEntitiesNotChangedThrowIncorrectParametersExceptionTest() {
        final Long id = 2L;

        doNothing().when(giftCertificateValidator).validateId(anyLong());
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate5));
        when(modelMapper.map(giftCertificate5, GiftCertificateDto.class)).thenReturn(giftCertificateDto6);

        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.patchUpdate(giftCertificateDto9, id));
    }

    @Test
    void deletePositiveTest() {
        final Long id = 2L;
        doNothing().when(giftCertificateValidator).validateId(anyLong());
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate3));
        when(modelMapper.map(giftCertificate3, GiftCertificateDto.class)).thenReturn(giftCertificateDto3);
        when(modelMapper.map(giftCertificateDto3, GiftCertificate.class)).thenReturn(giftCertificate3);

        doNothing().when(giftCertificateRepository).delete(giftCertificate3);

        assertDoesNotThrow(() -> giftCertificateService.delete(id));
    }

    @Test
    void deleteGiftCertificateThrowIncorrectParameterValueExceptionTest() {
        final long id = -5;
        doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validateId(anyLong());
        assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.delete(id));
    }

    @Test
    public void deleteGiftCertificateThrowResourceNotFoundExceptionTest() {
        final Long id = 10L;
        doNothing().when(giftCertificateValidator).validateId(anyLong());
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class, () -> giftCertificateService.delete(id));
    }
}