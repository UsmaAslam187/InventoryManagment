package com.techfoot.stockspree.Business.Store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techfoot.stockspree.Business.DataContracts.CreateProducts.CreateProductsInput_IP;
import com.techfoot.stockspree.Business.DataContracts.CreateProducts.CreateProductsOutput_IP;
import com.techfoot.stockspree.Business.DataContracts.GetAllProducts.GetAllProductsInput_IP;
import com.techfoot.stockspree.Business.DataContracts.GetAllProducts.GetAllProductsOutput_IP;
import com.techfoot.stockspree.Business.DataContracts.GetSingleProduct.GetSingleProductInput_IP;
import com.techfoot.stockspree.Business.DataContracts.GetSingleProduct.GetSingleProductOutput_IP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductsOP.Input_CreateProductsOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductsOP.Output_CreateProductsOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductsOP.Port_CreateProductsOP;
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
    private Port_CreateProductsOP port_CreateProductsOP;

    @Autowired
    private Port_GetAllProductsOP port_GetAllProductsOP;

    @Autowired
    private Port_GetSingleProductOP port_GetSingleProductOP;

    @Autowired
    private Port_GetAccountsOP port_GetAccountsOP;

    @Transactional
    public CreateProductsOutput_IP createProducts(CreateProductsInput_IP input) {
        try {
            List<String> errors = new ArrayList<>();
            boolean overallSuccess = true;

            for (CreateProductsInput_IP.Product product : input.getProducts()) {
                if (product.getSalesAccount() != null) {
                    Output_GetAccountsOP accounts = port_GetAccountsOP
                            .getAccounts(new Input_GetAccountsOP(product.getSalesAccount()));
                    System.out.println("accounts: " + accounts);
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
                Input_CreateProductsOP outboundInput = new Input_CreateProductsOP();
                outboundInput.setName(product.getName());
                outboundInput.setCode(product.getCode());
                outboundInput.setPrice(product.getPrice());
                outboundInput.setTax(product.getTax());
                outboundInput.setType(product.getType());
                outboundInput.setSalesAccount(product.getSalesAccount());
                outboundInput.setPurchaseAccount(product.getPurchaseAccount());
                Output_CreateProductsOP output = port_CreateProductsOP.createProducts(outboundInput);
                if (!output.getSuccess()) {
                    errors.add(output.getMessage());
                    overallSuccess = false;
                }
            }
            return new CreateProductsOutput_IP(overallSuccess, errors.isEmpty() ? null : errors.toString(), null);
        } catch (Exception e) {
            return new CreateProductsOutput_IP(false, e.getMessage(), null);
        }

    }

    @Transactional
    public GetAllProductsOutput_IP getAllProducts(GetAllProductsInput_IP input) {
        try {
            Input_GetAllProductsOP.Page page = null;
            System.out.println("input.getPage(): " + input.getPage());
            if (input.getPage() != null) {
                page = new Input_GetAllProductsOP.Page();
                page.setSize(input.getPage().getSize());
                page.setTotalElements(input.getPage().getTotalElements());
                page.setTotalPages(input.getPage().getTotalPages());
                page.setPageNumber(input.getPage().getPageNumber());
                page.setSearchedValue(input.getPage().getSearchedValue());
                page.setCsvExport(input.getPage().getCsvExport());
            }
            Output_GetAllProductsOP products = port_GetAllProductsOP
                    .getAllProducts(new Input_GetAllProductsOP(page, input.getSearchedValue(), input.getNameFlag()));
            if (products.getSuccess()) {
                List<GetAllProductsOutput_IP.Product> productList = new ArrayList<>();
                if (products.getProducts() != null) {
                    for (var dbProduct : products.getProducts()) {
                        GetAllProductsOutput_IP.Product product = new GetAllProductsOutput_IP.Product();
                        product.setCode(dbProduct.getCode());
                        product.setName(dbProduct.getName());
                        product.setPrice(dbProduct.getPrice());
                        product.setTax(dbProduct.getTax());
                        product.setType(dbProduct.getType());
                        product.setSalesAccount(dbProduct.getSalesAccount());
                        product.setPurchaseAccount(dbProduct.getPurchaseAccount());
                        productList.add(product);
                    }
                }
                GetAllProductsOutput_IP.Page responsePage = new GetAllProductsOutput_IP.Page();
                responsePage.setSize(products.getPage().getSize());
                responsePage.setTotalElements(products.getPage().getTotalElements());
                responsePage.setTotalPages(products.getPage().getTotalPages());
                responsePage.setPageNumber(products.getPage().getPageNumber());
                responsePage.setSearchedValue(products.getPage().getSearchedValue());
                responsePage.setCsvExport(products.getPage().getCsvExport());
                return new GetAllProductsOutput_IP(true, "Products retrieved successfully", new ArrayList<>(),
                        productList, responsePage);
            } else {
                return new GetAllProductsOutput_IP(false, products.getMessage(), products.getErrors(), null, null);
            }
        } catch (Exception e) {
            return new GetAllProductsOutput_IP(false, e.getMessage(), new ArrayList<>(Arrays.asList(e.getMessage())), null, null);
        }
    }

    @Transactional
    public GetSingleProductOutput_IP getSingleProduct(GetSingleProductInput_IP input) {
        try {
            Output_GetSingleProductOP products = port_GetSingleProductOP
                    .getSingleProduct(new Input_GetSingleProductOP(input.getCode(), input.getName()));
            Output_GetSingleProductOP.Product dbProduct = products.getProduct();
            if (products.getSuccess()) {
                GetSingleProductOutput_IP.Product product = new GetSingleProductOutput_IP.Product();
                product.setCode(dbProduct.getCode());
                product.setName(dbProduct.getName());
                product.setPrice(dbProduct.getPrice());
                product.setTax(dbProduct.getTax());
                product.setType(dbProduct.getType());
                product.setSalesAccount(dbProduct.getSalesAccount());
                product.setPurchaseAccount(dbProduct.getPurchaseAccount());
                return new GetSingleProductOutput_IP(products.getSuccess(), products.getMessage(), products.getErrors(), product);
            } else {
                return new GetSingleProductOutput_IP(false, products.getMessage(), products.getErrors(), null);
            }
        } catch (Exception e) {
            return new GetSingleProductOutput_IP(false, e.getMessage(), new ArrayList<>(Arrays.asList(e.getMessage())), null);
        }
    }
}
