package com.techfoot.stockspree.OutboundAdaptors.REST.Accounting.Q_GetAccounts.Accounting_Contracts;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    @JsonProperty("success")
    private boolean success;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("statusCode")
    private int statusCode;
    
    @JsonProperty("data")
    private JsonNode data;
    
    @JsonProperty("error")
    private String error;
}
