package com.techfoot.stockspree.Business.DataContracts.CreateProduct;
import java.util.List;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductOutput_IP {
    private Boolean success;
    private String message;
    private List<String> errors;
}