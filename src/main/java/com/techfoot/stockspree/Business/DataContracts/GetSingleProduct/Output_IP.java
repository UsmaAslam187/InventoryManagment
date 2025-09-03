package com.techfoot.stockspree.Business.DataContracts.GetSingleProduct;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Output_IP {
    private Boolean success;
    private String message;
    private List<String> errors;
    private Product product;
    private List<Product> products; // Added for multiple products

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Product {
        private String code;
        private String name;
        private Integer price;
        private Integer salesAccount;
        private Integer purchaseAccount;
    }

}