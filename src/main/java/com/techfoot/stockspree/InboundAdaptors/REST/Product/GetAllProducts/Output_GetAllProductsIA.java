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
    private DataWrapper data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataWrapper {
        private List<Product> products;
        private Page page;
    }

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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Page {
        private Integer size;
        private Integer totalElements;
        private Integer totalPages;
        private Integer pageNumber;
        private String searchedValue;
        private Boolean csvExport;
    }
}
