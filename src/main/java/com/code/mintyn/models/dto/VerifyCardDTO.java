package com.code.mintyn.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
@AllArgsConstructor
@Builder
public class VerifyCardDTO {
    @NotEmpty(message = "bin must be provided")
    private String bin;
    private String provider;
}
