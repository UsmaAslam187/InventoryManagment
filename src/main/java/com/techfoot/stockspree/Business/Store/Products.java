package com.techfoot.stockspree.Business.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techfoot.stockspree.Business.DataContracts.CreateProduct.CreateProductInput_IP;
import com.techfoot.stockspree.Business.DataContracts.CreateProduct.CreateProductOutput_IP;
import com.techfoot.stockspree.Business.DataContracts.GetAllProducts.GetAllProductsInput_IP;
import com.techfoot.stockspree.Business.DataContracts.GetAllProducts.GetAllProductsOutput_IP;
import com.techfoot.stockspree.Business.DataContracts.GetSingleProduct.GetSingleProductInput_IP;
import com.techfoot.stockspree.Business.DataContracts.GetSingleProduct.GetSingleProductOutput_IP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP.Input_CreateProductOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP.Output_CreateProductOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP.Port_CreateProductOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.GetAllProductsOP.Input_GetAllProductsOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.GetAllProductsOP.Output_GetAllProductsOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.GetAllProductsOP.Port_GetAllProductsOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.GetSingleProductOP.Input_GetSingleProductOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.GetSingleProductOP.Output_GetSingleProductOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.GetSingleProductOP.Port_GetSingleProductOP;
import com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts.Input_GetAccountsOP;
import com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts.Output_GetAccountsOP;
import com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts.Port_GetAccountsOP;

@Service
public class Products {
    @Autowired
    private Port_CreateProductOP port_CreateProductOP;

    @Autowired
    private Port_GetAllProductsOP port_GetAllProductsOP;

    @Autowired
    private Port_GetSingleProductOP port_GetSingleProductOP;

    @Autowired
    private Port_GetAccountsOP port_GetAccountsOP;

    @Transactional
    public CreateProductOutput_IP createProduct(CreateProductInput_IP input) {
        try {
            List<String> errors = new ArrayList<>();
            boolean overallSuccess = true;

            for (CreateProductInput_IP.Product product : input.getProducts()) {
                if (product.getSalesAccount() != null) {
                    Output_GetAccountsOP accounts = port_GetAccountsOP
                            .getAccounts(new Input_GetAccountsOP(product.getSalesAccount()));
                    if (!accounts.getSuccess()) {
                        errors.add("Sales account not found");
                        overallSuccess = false;
                    }
                }
                if (product.getPurchaseAccount() != null) {
                    Output_GetAccountsOP accounts = port_GetAccountsOP
                            .getAccounts(new Input_GetAccountsOP(product.getPurchaseAccount()));
                    if (!accounts.getSuccess()) {
                        errors.add("Purchase account not found");
                        overallSuccess = false;
                    }
                }
                Input_CreateProductOP outboundInput = new Input_CreateProductOP();
                outboundInput.setName(product.getName());
                outboundInput.setCode(product.getCode());
                outboundInput.setPrice(product.getPrice());
                outboundInput.setTax(product.getTax());
                outboundInput.setType(product.getType());
                outboundInput.setSalesAccount(product.getSalesAccount());
                outboundInput.setPurchaseAccount(product.getPurchaseAccount());
                Output_CreateProductOP output = port_CreateProductOP.createProduct(outboundInput);
                if (!output.getSuccess()) {
                    errors.add(output.getMessage());
                    overallSuccess = false;
                }
            }
            return new CreateProductOutput_IP(overallSuccess, errors.isEmpty() ? null : errors.toString(), null);
        } catch (Exception e) {
            return new CreateProductOutput_IP(false, e.getMessage(), null);
        }

    }

    @Transactional
    public GetAllProductsOutput_IP getAllProducts(GetAllProductsInput_IP input) {
        try {
            Output_GetAllProductsOP products = port_GetAllProductsOP
                    .getAllProducts(new Input_GetAllProductsOP(input.getPage(),input.getSearchedValue()));
            if (products.getSuccess()) {
                return new GetAllProductsOutput_IP(true, "Products retrieved successfully", null,
                        products.getProducts());
            } else {
                return new GetAllProductsOutput_IP(false, products.getMessage(), null, null);
            }
        } catch (Exception e) {
            return new GetAllProductsOutput_IP(false, e.getMessage(), null, null);
        }
    }

    @Transactional
    public GetSingleProductOutput_IP getSingleProduct(GetSingleProductInput_IP input) {
        try {
            Output_GetSingleProductOP products = port_GetSingleProductOP
                    .getSingleProduct(new Input_GetSingleProductOP(input.getCode(), input.getName()));
            GetAllProductsOutput_IP.Product dbProduct = products.getProduct();
            if (products.getSuccess()) {
                return new GetSingleProductOutput_IP(true, "Products retrieved successfully", products.getProduct());
            } else {
                return new GetSingleProductOutput_IP(false, products.getMessage(), null, null);
            }
        } catch (Exception e) {
            return new GetSingleProductOutput_IP(false, e.getMessage(), null, null);
        }
    }
}
