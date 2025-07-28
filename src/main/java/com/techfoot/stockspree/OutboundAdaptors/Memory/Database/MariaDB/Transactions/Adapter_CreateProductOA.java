package com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techfoot.stockspree.InboundAdaptors.REST.C_CreateProduct.Input_CreateProductIA;
import com.techfoot.stockspree.InboundAdaptors.REST.C_CreateProduct.Output_CreateProductIA;
import com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Queries.Queries;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.Port_CreateProductOP;

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
    public Output_CreateProductIA createProduct(Input_CreateProductIA input) {
        String query = queries.getInsertProductQuery("stockspree");
        entityManager.createNativeQuery(query)
                .setParameter("name", input.getName())
                .setParameter("description", input.getDescription())
                .setParameter("price", input.getPrice())
                .executeUpdate();
        
        // Get the last inserted ID
        Integer productId = getLastInsertedId(input.getName());
        
        return new Output_CreateProductIA(productId, input.getName(), input.getPrice(), input.getDescription());
    }

    @Transactional(readOnly = true)
    public Integer getLastInsertedId(String productName) {
        String query = queries.getSelectProductIdByNameQuery("stockspree");
        return (Integer) entityManager.createNativeQuery(query)
                .setParameter("name", productName)
                .getSingleResult();
    }
}    