package com.techfoot.stockspree.Business.DataContracts.GetAllProducts;

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
public class GetAllProductsInput_IP {

    private Page page;
    private String searchedValue;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Page {
        private Integer size;
        private Integer totalElements;
        private Integer totalPages;
        private Integer pageNumber;
        private String searchedValue;
        private Boolean csvExport;
    }
}
