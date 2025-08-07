package com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Input_CreateProductOP {
    private String name;
    private String code;
    private Double price;
    private String tax;
    private String type;
    private Integer salesAccount;
    private Integer purchaseAccount;
} 