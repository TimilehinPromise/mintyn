package com.code.mintyn.provider.Binlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude
public class CardResponse {
    private NumberDetails number;
    private String scheme;
    private String type;
    private String brand;
    private boolean prepaid;
    private CountryDetails country;
    private BankDetails bank;
}
