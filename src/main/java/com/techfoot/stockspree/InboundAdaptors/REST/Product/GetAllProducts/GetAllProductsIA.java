package com.techfoot.stockspree.InboundAdaptors.REST.Product.GetAllProducts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techfoot.stockspree.Business.DataContracts.GetAllProducts.GetAllProductsInput_IP;
import com.techfoot.stockspree.Business.DataContracts.GetAllProducts.GetAllProductsOutput_IP;
import com.techfoot.stockspree.InboundAdaptors.Configurations.CustomHttpRequestWrapper;
import com.techfoot.stockspree.InboundAdaptors.Configurations.RequestContext;
import com.techfoot.stockspree.InboundAdaptors.Configurations.SharedCustomDeserializer;
import com.techfoot.stockspree.InboundPort.Product.Q_GetAllProductsHandler;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class GetAllProductsIA {

    @Autowired
    private Q_GetAllProductsHandler handler;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SharedCustomDeserializer sharedDeserializer;

    public Output_GetAllProductsIA getAllProducts(HttpServletRequest request) {

        String action = RequestContext.getCurrentAction();
        if (!"Retrieve Products".equals(action)) {
            return null;
        }

        CustomHttpRequestWrapper wrappedRequest = (CustomHttpRequestWrapper) request.getAttribute("wrappedRequest");
        String requestBody = wrappedRequest != null ? wrappedRequest.getBody() : null;

        SharedCustomDeserializer.DeserializationResult<Input_GetAllProductsIA> deserializationResult = sharedDeserializer
                .deserialize(requestBody, Input_GetAllProductsIA.class, objectMapper);

        if (deserializationResult.hasErrors() || !deserializationResult.getValidationErrors().isEmpty()) {
            return new Output_GetAllProductsIA(false, "Input validation failed",
                    deserializationResult.getValidationErrors(), null);
        }

        Input_GetAllProductsIA input = deserializationResult.getResult();
        GetAllProductsInput_IP inputIP = new GetAllProductsInput_IP();
        inputIP.setNameFlag(input.getNameFlag());
        if (input.getSearchedValue() != null) {
            inputIP.setSearchedValue(input.getSearchedValue());
        }if (input.getPage() != null) {
            inputIP.setPage(
                    new GetAllProductsInput_IP.Page(input.getPage().getSize(), input.getPage().getTotalElements(),
                            input.getPage().getTotalPages(), input.getPage().getPageNumber(),
                            input.getPage().getSearchedValue(),
                            input.getPage().getCsvExport()));
        } else {
            return new Output_GetAllProductsIA(false, "Input validation failed",
                    Arrays.asList("SearchedValue or Page is required"), null);
        }
        GetAllProductsOutput_IP outputIP = handler.getAllProducts(inputIP);

        List<Output_GetAllProductsIA.Product> products = new ArrayList<Output_GetAllProductsIA.Product>();
        if (outputIP.getProducts() != null) {
            for (GetAllProductsOutput_IP.Product ipProduct : outputIP.getProducts()) {
                Output_GetAllProductsIA.Product product = new Output_GetAllProductsIA.Product();
                product.setCode(ipProduct.getCode());
                product.setName(ipProduct.getName());
                product.setPrice(ipProduct.getPrice());
                product.setTax(ipProduct.getTax());
                product.setType(ipProduct.getType());
                product.setSalesAccount(ipProduct.getSalesAccount());
                product.setPurchaseAccount(ipProduct.getPurchaseAccount());
                products.add(product);
            }
        }

        // Map Page object from business layer to adapter layer
        Output_GetAllProductsIA.Page page = null;
        if (outputIP.getPage() != null) {
            page = new Output_GetAllProductsIA.Page();
            page.setSize(outputIP.getPage().getSize());
            page.setTotalElements(outputIP.getPage().getTotalElements());
            page.setTotalPages(outputIP.getPage().getTotalPages());
            page.setPageNumber(outputIP.getPage().getPageNumber());
            page.setSearchedValue(outputIP.getPage().getSearchedValue());
            page.setCsvExport(outputIP.getPage().getCsvExport());
        }

        return new Output_GetAllProductsIA(outputIP.getSuccess(), outputIP.getMessage(),
                outputIP.getErrors(), new Output_GetAllProductsIA.DataWrapper(products, page));
    }
}