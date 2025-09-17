package com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_ScheduleProcess.Scheduler_Contracts;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private String workspace;
    private String email;
    private ProcessDetail processDetail;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessDetail {
        private String title; // Process title (e.g., "Upload Customers")
        private String status;

        @JsonProperty("data") // Status of the process (e.g., "Pending")
        private ProcessData data; // Nested data object
        private String timeToStart; // Scheduled start time (e.g., "1735812060")
        private int processId; // Unique process identifier
        private int partyId; // Party ID (e.g., 0)
        private Integer businessUnitId; // Business unit ID (e.g., 1)
        private String date; // Date as a string (e.g., "2025-01-02")
        private String time; // Time as a string (e.g., "15:01")
        private int serviceCode; // Service code (e.g., 200)
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessData {
        private String projects; // CSV string of projects
        private String workspace; // Workspace name (e.g., "test")
    }
}
