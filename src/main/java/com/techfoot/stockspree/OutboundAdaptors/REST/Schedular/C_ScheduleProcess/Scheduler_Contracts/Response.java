package com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_ScheduleProcess.Scheduler_Contracts;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    private boolean success;             // Indicates if the operation succeeded
    private List<String> error;         // List of errors (can be empty if no errors)
    private ResponseData data;          // Nested data object
    private int statusCode;             // HTTP status code
    private String message;             // Additional message (can be empty)

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseData {
        private int registerProcess;    // Process ID, represented as an integer
    }
}
