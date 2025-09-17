package com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_UpdateScheduledProcess.UpdateProcess_Contracts;

import lombok.Data;

@Data
public class Request {
    private ProcessDetail process;
    private String email;
    private String workspace;

    @Data
    public static class ProcessDetail {
        private int instanceId;
        private String status;
        private String message;
    }
}
