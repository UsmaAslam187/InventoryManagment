package com.techfoot.stockspree.OutboundPort.RPC.REST.SchedulerPorts.C_ScheduleProductCreation;

import com.techfoot.stockspree.InboundAdaptors.REST.Product.CreateMultipleProducts.Input_CreateMultipleProductsIA;

public interface Port_ScheduleProductsCreationOP {
    public Output_ScheduleProductsCreationOP handleSchedule(Input_CreateMultipleProductsIA input);
}
