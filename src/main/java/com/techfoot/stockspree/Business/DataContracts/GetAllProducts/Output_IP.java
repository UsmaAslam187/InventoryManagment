package com.techfoot.stockspree.Business.DataContracts.GetAllProducts;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Output_IP {
    private Boolean success;
    private String message;
    private List<String> errors;
    private List<Product> products;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Product {
        private String code;
        private String name;
        private Integer price;
        private Integer salesAccount;
        private Integer purchaseAccount;
    }
}