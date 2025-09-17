package com.techfoot.stockspree.OutboundPort.RPC.REST.SchedulerPorts.C_UpdateScheduledProcess;

import com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_UpdateScheduledProcess.UpdateProcess_Contracts.Request;
import com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_UpdateScheduledProcess.UpdateProcess_Contracts.Response;

public interface Port_UpdateScheduledProcessOP {
    public Response handleUpdateScheduledProcess(Request input);
}
