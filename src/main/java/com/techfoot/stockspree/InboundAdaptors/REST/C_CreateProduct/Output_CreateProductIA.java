package com.techfoot.stockspree.InboundAdaptors.REST.C_CreateProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
    public class Output_CreateProductIA {
    private Long id;
    private String name;
    private Double price;
    private String description;
}
