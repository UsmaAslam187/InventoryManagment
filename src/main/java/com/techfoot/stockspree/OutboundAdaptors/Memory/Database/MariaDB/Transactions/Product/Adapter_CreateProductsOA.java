package com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Transactions.Product;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techfoot.stockspree.InboundAdaptors.Configurations.WorkspaceContext;
import com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Queries.Queries;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductsOP.Input_CreateProductsOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductsOP.Output_CreateProductsOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductsOP.Port_CreateProductsOP;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class Adapter_CreateProductsOA implements Port_CreateProductsOP {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Queries queries;

    @Override
    @Transactional
    public Output_CreateProductsOP createProducts(Input_CreateProductsOP input) {
        try {
            // Validate input
            if (input == null) {
                return new Output_CreateProductsOP(
                        false,
                        "Input is null",
                        new ArrayList<>(Arrays.asList("Product input cannot be null")));
            }
            String query = queries.getInsertProductQuery(WorkspaceContext.getCurrentDatabase());
            entityManager.createNativeQuery(query)
                    .setParameter("name", input.getName())
                    .setParameter("code", input.getCode())
                    .setParameter("tax", input.getTax())
                    .setParameter("type", input.getType())
                    .setParameter("salesAccount", input.getSalesAccount())
                    .setParameter("purchaseAccount", input.getPurchaseAccount())
                    .setParameter("price", input.getPrice())
                    .executeUpdate();

            return new Output_CreateProductsOP(
                    true,
                    "Product created successfully",
                    new ArrayList<>());

        } catch (Exception e) {
            return new Output_CreateProductsOP(
                    false,
                    "Product creation failed",
                    new ArrayList<>(Arrays.asList(e.getMessage())));
        }
    }
}