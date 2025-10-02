package com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_UpdateScheduledProcess;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techfoot.stockspree.InboundAdaptors.Configurations.WorkspaceContext;
import com.techfoot.stockspree.OutboundAdaptors.Configurations.TechfootServicesConfig;
import com.techfoot.stockspree.OutboundPort.RPC.REST.SchedulerPorts.C_UpdateScheduledProcess.Port_UpdateScheduledProcessOP;
import com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_UpdateScheduledProcess.UpdateProcess_Contracts.Request;
import com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_UpdateScheduledProcess.UpdateProcess_Contracts.Response;

@Service
@Profile("prod")
public class Adapter_UpdateScheduledProcessOA_Prod implements Port_UpdateScheduledProcessOP {

    private final TechfootServicesConfig.ProcessManagerService service;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public Adapter_UpdateScheduledProcessOA_Prod() {
        this.service = new TechfootServicesConfig().processManagerService();
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Override
    public Response handleUpdateScheduledProcess(Request request) {
        try {
            // Convert the Request object to JSON-compatible structure
            request.setEmail("test@test.com");
            request.setWorkspace(WorkspaceContext.getCurrentWorkspace());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Request> entity = new HttpEntity<>(request, headers);

            // Build the target URL
            String url = String.format("http://%s:%d/flowspree/tasks/update-task-status", service.getHostName(), service.getPortNumber());

            // Make the HTTP call
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(url, entity, String.class);

            // Parse the response into a Response object
            return objectMapper.readValue(apiResponse.getBody(), Response.class);
        } catch (JsonProcessingException | RestClientException e) {
            // Return a failure response in case of an exception
            Response errorResponse = new Response();
            errorResponse.setSuccess(false);
            errorResponse.setStatus("Failure");
            errorResponse.setMessage("Failed to update the scheduled process: " + e.getMessage());
            return errorResponse;
        }
    }
}
