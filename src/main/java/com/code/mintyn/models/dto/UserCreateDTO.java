package com.code.mintyn.models.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserCreateDTO {
    private String userName;
    private String password;
}
