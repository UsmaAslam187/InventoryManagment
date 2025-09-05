package com.techfoot.stockspree.InboundAdaptors.REST.Product.GetAllProducts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Input_GetAllProductsIA {
    @JsonProperty("method")
    private String method;

    @JsonProperty("action")
    private String action;

    @JsonProperty("apiVersion")
    private String apiVersion;

    @JsonProperty(value = "page", required = true)

    private Page page;

    @JsonProperty(value = "searchedValue", required = true)
    private String searchedValue;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Page {
        @JsonProperty("size")
        private Integer size;

        @JsonProperty("totalElements")
        private Integer totalElements;

        @JsonProperty("totalPages")
        private Integer totalPages;

        @JsonProperty("pageNumber")
        private Integer pageNumber;

        @JsonProperty("searchedValue")
        private String searchedValue;

        @JsonProperty("csvExport")
        private Boolean csvExport;
    }
}