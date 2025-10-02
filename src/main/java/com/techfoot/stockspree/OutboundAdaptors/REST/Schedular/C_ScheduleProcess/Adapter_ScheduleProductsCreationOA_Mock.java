package com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_ScheduleProcess;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.techfoot.stockspree.InboundAdaptors.REST.Product.CreateMultipleProducts.Input_CreateMultipleProductsIA;
import com.techfoot.stockspree.OutboundPort.RPC.REST.SchedulerPorts.C_ScheduleProductCreation.Output_ScheduleProductsCreationOP;
import com.techfoot.stockspree.OutboundPort.RPC.REST.SchedulerPorts.C_ScheduleProductCreation.Port_ScheduleProductsCreationOP;

@Service
@Profile("mock")
public class Adapter_ScheduleProductsCreationOA_Mock implements Port_ScheduleProductsCreationOP {

    @Override
    public Output_ScheduleProductsCreationOP handleSchedule(Input_CreateMultipleProductsIA input) {
        // Mocked success response
        return new Output_ScheduleProductsCreationOP("success", 12345, "Products scheduled successfully");
    }
}
