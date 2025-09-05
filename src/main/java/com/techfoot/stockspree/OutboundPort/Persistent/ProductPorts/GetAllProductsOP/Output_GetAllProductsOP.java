package com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.GetAllProductsOP;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Output_GetAllProductsOP {
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
