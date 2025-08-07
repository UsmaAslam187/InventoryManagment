package com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Output_CreateProductOP {
    private Boolean success;
    private String message;
    private List<String> errors;
} 