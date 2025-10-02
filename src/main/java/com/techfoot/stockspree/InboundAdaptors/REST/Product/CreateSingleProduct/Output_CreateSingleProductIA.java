package com.techfoot.stockspree.InboundAdaptors.REST.Product.CreateSingleProduct;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Output_CreateSingleProductIA {
    private Boolean success;
    private String message;
    private List<String> errors;
}
