package com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Transactions.Product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techfoot.stockspree.InboundAdaptors.Configurations.WorkspaceContext;
import com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Queries.Queries;
import com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Tables.Product_Table;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.GetAllProductsOP.Input_GetAllProductsOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.GetAllProductsOP.Output_GetAllProductsOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.GetAllProductsOP.Port_GetAllProductsOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.GetSingleProductOP.Input_GetSingleProductOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.GetSingleProductOP.Output_GetSingleProductOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.GetSingleProductOP.Port_GetSingleProductOP;
import com.techfoot.stockspree.Utils.golbalMethods;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class Adapter_GetProductsOA implements Port_GetAllProductsOP, Port_GetSingleProductOP {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Queries queries;

    @Override
    public Output_GetAllProductsOP getAllProducts(Input_GetAllProductsOP input) {
        try {
            Long count = 0L;
            if (input.getSearchedValue() != null) {
                String joinQuery = queries.getCountSpecificProductsQuery(WorkspaceContext.getCurrentDatabase(),
                        input.getSearchedValue(), input.getNameFlag());
                count = (Long) entityManager.createNativeQuery(joinQuery).getSingleResult();
                if (count == 0) {
                    return new Output_GetAllProductsOP(true, "No products found", new ArrayList<>(), null, new Output_GetAllProductsOP.Page(input.getPage().getSize(), count.intValue(),
                            (int) Math.ceil((double) count.intValue() / input.getPage().getSize()),
                            input.getPage().getPageNumber(), input.getSearchedValue(), input.getPage().getCsvExport()));
                }
            } else {
                String joinQuery = queries.getAllProductsCount(WorkspaceContext.getCurrentDatabase());
                count = (Long) entityManager.createNativeQuery(joinQuery).getSingleResult();
                if (count == 0) {
                    return new Output_GetAllProductsOP(true, "No products found", new ArrayList<>(), null, new Output_GetAllProductsOP.Page(input.getPage().getSize(), count.intValue(),
                            (int) Math.ceil((double) count.intValue() / input.getPage().getSize()),
                            input.getPage().getPageNumber(), input.getSearchedValue(), input.getPage().getCsvExport()));
                }
            }

            golbalMethods globalMethods = golbalMethods.getPage(input.getPage().getPageNumber(),
                    input.getPage().getSize(), count.intValue());
            String query = "";
            if (input.getSearchedValue() != null) {
                if (input.getNameFlag()) {
                    query = queries.getSpecificProductsQuery(WorkspaceContext.getCurrentDatabase(),
                            input.getSearchedValue(), input.getNameFlag());
                } else {
                    query = queries.getSpecificProductsQuery(WorkspaceContext.getCurrentDatabase(),
                            input.getSearchedValue(), input.getNameFlag());
                }
            } else {
                query = queries.getSelectAllProductsQuery(WorkspaceContext.getCurrentDatabase(),
                        globalMethods.getStart(), globalMethods.getEnd());
            }
            List<Product_Table> products = entityManager.createNativeQuery(query, Product_Table.class).getResultList();
            List<Output_GetAllProductsOP.Product> productsOutput = new ArrayList<>();
            for (Product_Table product : products) {
                Output_GetAllProductsOP.Product productOutput = new Output_GetAllProductsOP.Product();
                productOutput.setCode(product.getCode());
                productOutput.setName(product.getName());
                productOutput.setPrice(product.getPrice());
                productOutput.setTax(product.getTax());
                productOutput.setType(product.getType());
                productOutput.setSalesAccount(product.getSalesAccount());
                productOutput.setPurchaseAccount(product.getPurchaseAccount());
                productsOutput.add(productOutput);
            }
            return new Output_GetAllProductsOP(true, "Products retrieved successfully", new ArrayList<>(),
                    productsOutput, new Output_GetAllProductsOP.Page(input.getPage().getSize(), count.intValue(),
                            (int) Math.ceil((double) count.intValue() / input.getPage().getSize()),
                            input.getPage().getPageNumber(), input.getSearchedValue(), input.getPage().getCsvExport()));
        } catch (Exception e) {
            return new Output_GetAllProductsOP(false, e.getMessage(), new ArrayList<>(), null, null);
        }
    }

    @Override
    public Output_GetSingleProductOP getSingleProduct(Input_GetSingleProductOP input) {
        try {
            String query = queries.getSelectProductByCodeOrNameQuery(WorkspaceContext.getCurrentDatabase(),
                    input.getCode(), input.getName());
            Product_Table product = (Product_Table) entityManager
                    .createNativeQuery(query, Product_Table.class).setParameter("name", input.getName())
                    .setParameter("code", input.getCode()).getSingleResult();
            // Output_GetSingleProductOP output = new Output_GetSingleProductOP();
            Output_GetSingleProductOP.Product productOutput = new Output_GetSingleProductOP.Product();
            productOutput.setCode(product.getCode());
            productOutput.setName(product.getName());
            productOutput.setPrice(product.getPrice());
            productOutput.setTax(product.getTax());
            productOutput.setType(product.getType());
            productOutput.setSalesAccount(product.getSalesAccount());
            productOutput.setPurchaseAccount(product.getPurchaseAccount());
            return new Output_GetSingleProductOP(true, "Product retrieved successfully", new ArrayList<>(),
                    productOutput);
        } catch (Exception e) {
            return new Output_GetSingleProductOP(false, e.getMessage(), new ArrayList<>(), null);
        }
    }
}
