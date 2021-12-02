package com.epam.esm.dto.impl;

import com.epam.esm.dto.AbstractDto;
import com.epam.esm.dto.impl.deserializer.UserIdDeserializer;
import com.epam.esm.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
public class UserDto implements AbstractDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonDeserialize(using = UserIdDeserializer.class)
    private Long id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private RoleDto role;

    public UserDto(String username, String password){
        this.username=username;
        this.password=password;
    }
}
