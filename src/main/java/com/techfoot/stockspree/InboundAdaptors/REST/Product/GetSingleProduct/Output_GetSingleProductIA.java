package com.techfoot.stockspree.InboundAdaptors.REST.Product.GetSingleProduct;

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
public class Output_GetSingleProductIA {
    private boolean success;
    private String message;
    private List<String> error;
    private Product product;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product {
        private String code;
        private String name;
        private Double price;
        private String tax;
        private String type;
        private Integer salesAccount;
        private Integer purchaseAccount;

    }
}
