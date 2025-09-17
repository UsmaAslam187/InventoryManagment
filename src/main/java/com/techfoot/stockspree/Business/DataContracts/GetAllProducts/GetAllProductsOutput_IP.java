package com.techfoot.stockspree.Business.DataContracts.GetAllProducts;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllProductsOutput_IP {
    private Boolean success;
    private String message;
    private List<String> errors;
    private List<Product> products;
    private Page page;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
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
    @Getter
    public static class Page {
        private Integer size;
        private Integer totalElements;
        private Integer totalPages;
        private Integer pageNumber;
        private String searchedValue;
        private Boolean csvExport;
    }
}