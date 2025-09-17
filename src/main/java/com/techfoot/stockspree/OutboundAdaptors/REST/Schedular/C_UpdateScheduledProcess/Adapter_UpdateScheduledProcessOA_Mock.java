package com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_UpdateScheduledProcess;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.techfoot.stockspree.OutboundPort.RPC.REST.SchedulerPorts.C_UpdateScheduledProcess.Port_UpdateScheduledProcessOP;
import com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_UpdateScheduledProcess.UpdateProcess_Contracts.Request;
import com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_UpdateScheduledProcess.UpdateProcess_Contracts.Response;

@Service
@Profile("mock")
public class Adapter_UpdateScheduledProcessOA_Mock implements Port_UpdateScheduledProcessOP {

    @Override
    public Response handleUpdateScheduledProcess(Request request) {
        // Create a mock response
        Response mockResponse = new Response();
        mockResponse.setSuccess(true);
        mockResponse.setStatus("Success");
        mockResponse.setMessage("Successfully updated the scheduled process");
        return mockResponse;
    }
} 