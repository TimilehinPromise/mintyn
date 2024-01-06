package com.code.mintyn.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardStatsResponse {
    private boolean success;
    private int start;
    private int limit;
    private long size;
    private Map<String, Integer> payload;
}
