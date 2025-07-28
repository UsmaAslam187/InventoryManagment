package com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts;

import com.techfoot.stockspree.InboundAdaptors.REST.C_CreateProduct.Input_CreateProductIA;
import com.techfoot.stockspree.InboundAdaptors.REST.C_CreateProduct.Output_CreateProductIA;

public interface Port_CreateProductOP {

    Output_CreateProductIA createProduct(Input_CreateProductIA product);
}
