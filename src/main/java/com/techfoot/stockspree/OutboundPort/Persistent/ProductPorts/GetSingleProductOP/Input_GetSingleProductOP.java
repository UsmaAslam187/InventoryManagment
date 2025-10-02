package com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.GetSingleProductOP;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Input_GetSingleProductOP {
    private String code;
    private String name;
}
