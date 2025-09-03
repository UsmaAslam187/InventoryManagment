package com.techfoot.stockspree.InboundAdaptors.REST.Product.GetAllProducts;

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
public class Output_GetAllProductsIA {
    private boolean success;
    private String message;
    private List<String> error;
    private List<Product> products;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product {
        private String code;
        private String name;
        private Integer price;
        private Integer salesAccount;
        private Integer purchaseAccount;

    }
}
