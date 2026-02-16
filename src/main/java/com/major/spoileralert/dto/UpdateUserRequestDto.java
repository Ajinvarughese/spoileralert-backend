package com.major.spoileralert.dto;

import lombok.Data;

@Data
public class UpdateUserRequestDto {

    private String fullName;

    private String username;

    private String email;

    private String phone;
}
