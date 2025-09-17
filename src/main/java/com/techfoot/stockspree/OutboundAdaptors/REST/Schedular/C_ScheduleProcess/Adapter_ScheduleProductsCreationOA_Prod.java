package com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_ScheduleProcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techfoot.stockspree.InboundAdaptors.Configurations.RequestContext;
import com.techfoot.stockspree.InboundAdaptors.Configurations.WorkspaceContext;
import com.techfoot.stockspree.InboundAdaptors.REST.Product.CreateMultipleProducts.Input_CreateMultipleProductsIA;
import com.techfoot.stockspree.OutboundAdaptors.Configurations.TechfootServicesConfig;
import com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_ScheduleProcess.Scheduler_Contracts.Response;
import com.techfoot.stockspree.OutboundPort.RPC.REST.SchedulerPorts.C_ScheduleProductCreation.Output_ScheduleProductsCreationOP;
import com.techfoot.stockspree.OutboundPort.RPC.REST.SchedulerPorts.C_ScheduleProductCreation.Port_ScheduleProductsCreationOP;

@Service
@Profile("prod")
public class Adapter_ScheduleProductsCreationOA_Prod implements Port_ScheduleProductsCreationOP {

    private final TechfootServicesConfig.ProcessManagerService service;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public Adapter_ScheduleProductsCreationOA_Prod() {
        this.service = new TechfootServicesConfig().processManagerService();
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Override
    public Output_ScheduleProductsCreationOP handleSchedule(Input_CreateMultipleProductsIA request) {
        try {
            Map<String, Object> process = prepareProcessData(request);
            return executeRequest(process);
        } catch (Exception e) {
            return new Output_ScheduleProductsCreationOP("failure", 0, "Failed to schedule products");
        }
    }

    private Map<String, Object> prepareProcessData(Input_CreateMultipleProductsIA request) throws Exception {
        Input_CreateMultipleProductsIA.ProductData productsData = request.getData();
        System.out.println("productsData: " + productsData);
        String resDate = productsData.getScheduleTime().getDate();
        String time = convertTo24HourFormat(productsData.getScheduleTime().getTime());
        long timeToStartInt = getTimeInUnixFormat(resDate, time);

        Map<String, Object> productsDataMap = parseJson(productsData.getProductsData());
        productsDataMap.put("workspace", WorkspaceContext.getCurrentWorkspace());
        productsDataMap.put("action", RequestContext.getCurrentAction());
        productsDataMap.put("method", RequestContext.getCurrentMethod());
        Map<String, Object> processDetail = new HashMap<>();
        processDetail.put("title", "Create Products");
        processDetail.put("status", "Pending");
        processDetail.put("data", productsDataMap);
        processDetail.put("date", resDate);
        processDetail.put("time", time);
        processDetail.put("timeToStart", String.valueOf(timeToStartInt));
        processDetail.put("businessUnitId", 0);
        processDetail.put("partyId", 1);

        Map<String, Object> process = new HashMap<>();
        process.put("processDetail", processDetail);
        process.put("processId", 219);
        process.put("serviceCode", 200);
        process.put("title", "Upload Products");
        process.put("email", "almalik.umair@gmail.com");
        process.put("workspace", WorkspaceContext.getCurrentWorkspace());

        return process;
    }

    private Output_ScheduleProductsCreationOP executeRequest(Map<String, Object> process) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(process, headers);

        String url = String.format("http://%s:%d/flowspree/tasks/schedule-job", service.getHostName(),
                service.getPortNumber());
        ResponseEntity<String> apiResponse = restTemplate.postForEntity(url, entity, String.class);

        Response response = objectMapper.readValue(apiResponse.getBody(), Response.class);
        return response.isSuccess()
                ? new Output_ScheduleProductsCreationOP("success", response.getData().getRegisterProcess(),
                        response.getMessage())
                : new Output_ScheduleProductsCreationOP("failure", 0, response.getMessage());
    }

    private String convertTo24HourFormat(String time) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("h:mm a", java.util.Locale.ENGLISH);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime localTime = LocalTime.parse(time.toUpperCase().trim(), inputFormatter);
            return localTime.format(outputFormatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time format: " + time, e);
        }
    }

    private long getTimeInUnixFormat(String date, String time) {
        LocalDate localDate = LocalDate.parse(date);
        LocalTime localTime = LocalTime.parse(time);
        ZoneOffset zoneOffset = ZoneOffset.ofHours(5);
        return localDate.atTime(localTime).toEpochSecond(zoneOffset);
    }

    private Map<String, Object> parseJson(String jsonStr) {
        try {
            return objectMapper.readValue(jsonStr, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON string: " + e.getMessage());
        }
    }
}
