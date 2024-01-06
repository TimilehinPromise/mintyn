package com.code.mintyn.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginResponseModel {
    private String userName;
    private String token;
    private LocalDateTime expires;
}
