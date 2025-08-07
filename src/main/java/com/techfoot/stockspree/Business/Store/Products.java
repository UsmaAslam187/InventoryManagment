package com.techfoot.stockspree.Business.Store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techfoot.stockspree.InboundAdaptors.REST.Product.C_CreateProduct.Input_CreateProductIA;
import com.techfoot.stockspree.InboundAdaptors.REST.Product.C_CreateProduct.Output_CreateProductIA;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP.Input_CreateProductOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP.Output_CreateProductOP;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP.Port_CreateProductOP;

@Service
public class Products {
    @Autowired
    private Port_CreateProductOP port_CreateProductOP;

    public Output_CreateProductIA createProduct(Input_CreateProductIA input) {
        // Convert Inbound contract to Outbound contract
        Input_CreateProductOP outboundInput = new Input_CreateProductOP(
            input.getName(),
            input.getCode(),
            input.getPrice(),
            input.getTax(),
            input.getType(),
            input.getSalesAccount(),
            input.getPurchaseAccount()
        );
        
        // Call the outbound port
        Output_CreateProductOP outboundOutput = port_CreateProductOP.createProduct(outboundInput);
        
        // Convert Outbound contract back to Inbound contract
        return new Output_CreateProductIA(
            outboundOutput.getSuccess(),
            outboundOutput.getMessage(),
            outboundOutput.getErrors()
        );
    }
}
