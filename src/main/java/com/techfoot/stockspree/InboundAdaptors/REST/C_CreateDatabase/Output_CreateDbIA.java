package com.techfoot.stockspree.InboundAdaptors.REST.C_CreateDatabase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
@Getter
public class Output_CreateDbIA {
    private Boolean success;
    private String error;
    private String message;
}
