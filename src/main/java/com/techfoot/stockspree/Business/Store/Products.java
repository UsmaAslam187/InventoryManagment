package com.techfoot.stockspree.Business.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techfoot.stockspree.Business.DataContracts.CreateProduct.Input_IP;
import com.techfoot.stockspree.Business.DataContracts.CreateProduct.Output_IP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP.Input_CreateProductOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP.Output_CreateProductOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP.Port_CreateProductOP;
import com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts.Input_GetAccountsOP;
import com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts.Output_GetAccountsOP;
import com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts.Port_GetAccountsOP;

@Service
public class Products {
    @Autowired
    private Port_CreateProductOP port_CreateProductOP;

    @Autowired
    private Port_GetAccountsOP port_GetAccountsOP;

    @Transactional
    public Output_IP createProduct(Input_IP input) {
        try {
            List<String> errors = new ArrayList<>();
            boolean overallSuccess = true;
             
        for (Input_IP.Product product : input.getProducts()) {
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
        return new Output_IP(overallSuccess, errors.isEmpty() ? null : errors.toString(), null);
        } catch (Exception e) {
            return new Output_IP(false, e.getMessage(), null);
        }
       
    }
}
