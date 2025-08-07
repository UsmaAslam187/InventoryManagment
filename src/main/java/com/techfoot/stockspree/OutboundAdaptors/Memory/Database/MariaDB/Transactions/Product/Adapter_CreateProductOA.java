package com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Transactions.Product;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techfoot.stockspree.InboundAdaptors.Configurations.WorkspaceContext;
import com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Queries.Queries;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP.Input_CreateProductOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP.Output_CreateProductOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP.Port_CreateProductOP;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class Adapter_CreateProductOA implements Port_CreateProductOP {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Queries queries;

    @Override
    @Transactional
    public Output_CreateProductOP createProduct(Input_CreateProductOP input) {

        try {
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
        
        // Get the last inserted ID
        Integer productId = getLastInsertedId(input.getName());
        
        return new Output_CreateProductOP(
            true, 
            "Product created successfully", 
            new ArrayList<>()
        );
        } catch (Exception e) {
            return new Output_CreateProductOP(
                false, 
                "Product creation failed", 
                new ArrayList<>(Arrays.asList(e.getMessage()))
            );
        }
    }

    @Transactional(readOnly = true)
    public Integer getLastInsertedId(String productName) {
        String query = queries.getSelectProductIdByNameQuery(WorkspaceContext.getCurrentDatabase(), productName);
        return (Integer) entityManager.createNativeQuery(query)
                .setParameter("name", productName)
                .getSingleResult();
    }
}    