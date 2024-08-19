package com.abn.nl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ExchangeResponse {

    @JsonProperty("base")
    private String base;
    @JsonProperty("rates")
    private Map<String,Double> rates;
}
