package com.techfoot.stockspree.OutboundPort.RPC.REST.SchedulerPorts.C_ScheduleProductCreation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Output_ScheduleProductsCreationOP {
    private String status;
    private Integer instanceId;
    private String message;
}

