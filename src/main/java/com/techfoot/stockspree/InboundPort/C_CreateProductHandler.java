package com.techfoot.stockspree.InboundPort;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.techfoot.stockspree.Business.Domain.Product;
import com.techfoot.stockspree.InboundAdaptors.REST.C_CreateProduct.Input_CreateProductIA;
import com.techfoot.stockspree.InboundAdaptors.REST.C_CreateProduct.Output_CreateProductIA;

public class C_CreateProductHandler {
    private static final List<Product> products = new ArrayList<>();
    private static final AtomicLong idGenerator = new AtomicLong(1);

    public Output_CreateProductIA createProduct(Input_CreateProductIA input) {
        Long id = idGenerator.getAndIncrement();
        Product product = new Product(id, input.getName(), input.getPrice(), input.getDescription());
        products.add(product);
        System.out.println("Product created: " + product);
        return new Output_CreateProductIA(product.getId(), product.getName(), product.getPrice(), product.getDescription());
    }

    public static List<Product> getProducts() {
        return products;
    }
}
