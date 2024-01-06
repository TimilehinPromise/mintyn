package com.code.mintyn.models;

import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Mono;

@Data
@Builder
public class VerifySchemeModel  {
    private String bank;
    private String type;
    private String scheme;
}
