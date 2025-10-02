package com.techfoot.stockspree.Business.DataContracts.CreateProducts;
import java.util.List;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateProductsInput_IP {
    private List<Product> products;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Product {
        private String name;
        private String code;
        private Double price;
        private String tax;
        private String type;
        private Integer salesAccount;
        private Integer purchaseAccount;
    }
}


