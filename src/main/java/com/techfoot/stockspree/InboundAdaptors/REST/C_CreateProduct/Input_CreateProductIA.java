package com.techfoot.stockspree.InboundAdaptors.REST.C_CreateProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Input_CreateProductIA {
    private String name;
    private Double price;
    private String description;
}
