package com.techfoot.stockspree.Business.DataContracts.CreateProducts;
import java.util.List;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductsOutput_IP {
    private Boolean success;
    private String message;
    private List<String> errors;
}