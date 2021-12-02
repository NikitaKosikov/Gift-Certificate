package com.epam.esm.dto.impl;

import lombok.*;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokenDto {
    private String username;
    private String token;
    private long validityInMilliseconds;
}
