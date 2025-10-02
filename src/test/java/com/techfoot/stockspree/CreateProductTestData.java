package com.techfoot.stockspree;

import com.techfoot.stockspree.Business.DataContracts.CreateProducts.CreateProductsInput_IP;
import com.techfoot.stockspree.Business.DataContracts.CreateProducts.CreateProductsOutput_IP;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CreateProductTestData {

    private String title;
    private CreateProductsInput_IP products;
    private CreateProductsOutput_IP response;

    public String getTitle() {
        return title;
    }

    public CreateProductsInput_IP getProducts() {
        return products;
    }

    public CreateProductsOutput_IP getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "CreateProductTestData{"
                + ", title='" + title + '\''
                + ", products=" + products
                + ", response=" + response
                + '}';
    }
}
