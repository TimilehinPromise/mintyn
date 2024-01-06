package com.code.mintyn.provider.Binlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude
public class NumberDetails {
    private int length;
    private boolean luhn;
}
