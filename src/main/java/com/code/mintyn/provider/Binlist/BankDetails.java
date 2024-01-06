package com.code.mintyn.provider.Binlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude
public class BankDetails {
    private String name;
    private String url;
    private String phone;
    private String city;
}
