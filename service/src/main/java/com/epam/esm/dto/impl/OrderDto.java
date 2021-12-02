package com.epam.esm.dto.impl;

import com.epam.esm.dto.AbstractDto;
import com.epam.esm.dto.impl.deserializer.PriceDeserializer;
import com.epam.esm.util.ValidValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDto implements AbstractDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @JsonDeserialize(using = PriceDeserializer.class)
    private BigDecimal price;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ValidValue.FORMAT_DATE)
    private LocalDateTime purchaseTime;
    @JsonProperty(value = "gift_certificate_id")
    private Long giftCertificateId;
    @JsonProperty(value = "user_id")
    private Long userId;
}
