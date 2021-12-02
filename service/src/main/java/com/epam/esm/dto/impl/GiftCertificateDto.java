package com.epam.esm.dto.impl;

import com.epam.esm.dto.AbstractDto;
import com.epam.esm.dto.impl.deserializer.DurationDeserializer;
import com.epam.esm.dto.impl.deserializer.IdDeserializer;
import com.epam.esm.dto.impl.deserializer.PriceDeserializer;
import com.epam.esm.util.ValidValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GiftCertificateDto implements AbstractDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonDeserialize(using = IdDeserializer.class)
    private Long id;
    private String name;
    private String description;
    @JsonDeserialize(using = PriceDeserializer.class)
    private BigDecimal price;
    @JsonDeserialize(using = DurationDeserializer.class)
    private Integer duration;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ValidValue.FORMAT_DATE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ValidValue.FORMAT_DATE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastUpdateDate;
    private List<TagDto> tags = new ArrayList<>();
}
