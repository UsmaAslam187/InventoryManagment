package com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.CreateProductOP;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Input_CreateProductOP {
        private String name;
        private String code;
        private Double price;
        private String tax;
        private String type;
        private Integer salesAccount;
        private Integer purchaseAccount;
}   