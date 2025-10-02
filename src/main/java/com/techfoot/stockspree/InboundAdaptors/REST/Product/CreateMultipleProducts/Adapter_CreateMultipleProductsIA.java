package com.techfoot.stockspree.InboundAdaptors.REST.Product.CreateMultipleProducts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techfoot.stockspree.Business.DataContracts.CreateProducts.CreateProductsInput_IP;
import com.techfoot.stockspree.Business.DataContracts.CreateProducts.CreateProductsOutput_IP;
import com.techfoot.stockspree.InboundAdaptors.Configurations.CustomHttpRequestWrapper;
import com.techfoot.stockspree.InboundAdaptors.Configurations.SharedCustomDeserializer;
import com.techfoot.stockspree.InboundPort.Product.C_CreateProductsHandler;
import com.techfoot.stockspree.OutboundAdaptors.REST.Schedular.C_UpdateScheduledProcess.UpdateProcess_Contracts.Request;
import com.techfoot.stockspree.OutboundPort.RPC.REST.SchedulerPorts.C_ScheduleProductCreation.Output_ScheduleProductsCreationOP;
import com.techfoot.stockspree.OutboundPort.RPC.REST.SchedulerPorts.C_ScheduleProductCreation.Port_ScheduleProductsCreationOP;
import com.techfoot.stockspree.OutboundPort.RPC.REST.SchedulerPorts.C_UpdateScheduledProcess.Port_UpdateScheduledProcessOP;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class Adapter_CreateMultipleProductsIA {

    @Autowired
    private SharedCustomDeserializer sharedDeserializer;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Port_ScheduleProductsCreationOP port_ScheduleProductsCreationOP;

    @Autowired
    private Port_UpdateScheduledProcessOP port_UpdateScheduledProcessOP;

    @Autowired
    private C_CreateProductsHandler productHandler;  // Add this injection

    public Output_CreateMultipleProductsIA createMultipleProducts(HttpServletRequest request) throws IOException {
        CustomHttpRequestWrapper wrappedRequest = (CustomHttpRequestWrapper) request
                .getAttribute("wrappedRequest");
        String requestBody = wrappedRequest != null ? wrappedRequest.getBody() : null;

        // Use shared deserializer to handle all validation errors
        SharedCustomDeserializer.DeserializationResult<Input_CreateMultipleProductsIA> deserializationResult = sharedDeserializer
                .deserialize(requestBody, Input_CreateMultipleProductsIA.class, objectMapper);

        // Check if deserialization had errors
        if (deserializationResult.hasErrors() || !deserializationResult.getValidationErrors().isEmpty()) {
            return new Output_CreateMultipleProductsIA(false, "Invalid request", 400,
                    deserializationResult.getValidationErrors());
        }

        Input_CreateMultipleProductsIA input = deserializationResult.getResult();

        if (input.getOperation().equals("schedule")) {
            Output_ScheduleProductsCreationOP output = port_ScheduleProductsCreationOP.handleSchedule(input);
            return new Output_CreateMultipleProductsIA(true, output.getMessage(), 200, null, output.getInstanceId());
        }
        List<String> errorMessages = new ArrayList<>();
        boolean success = true;

        try {
            List<ProductsIA.Product> products;
            String[] errorList;
            String errorMessage;
            try {
                String productsJson = parseProductsStringToJson(input.getData().getProducts());
                SharedCustomDeserializer.DeserializationResult<ProductsIA> deserializationResult1 = sharedDeserializer.deserialize(productsJson, ProductsIA.class, objectMapper);

                // Check if deserialization had errors
                if (deserializationResult1.hasErrors() || !deserializationResult1.getValidationErrors().isEmpty()) {
                    updateProcessStatus(input, "Fail", String.join("; ", deserializationResult1.getValidationErrors()));
                    return new Output_CreateMultipleProductsIA(false, "Input validation failed", 400,
                            deserializationResult1.getValidationErrors());
                }

                products = deserializationResult1.getResult().getProducts();
                CreateProductsInput_IP inputIP = new CreateProductsInput_IP();
                inputIP.setProducts(products.stream()
                        .map(product -> new CreateProductsInput_IP.Product(product.getName(), product.getCode(),
                        product.getPrice(), product.getTax(), product.getType(),
                        product.getSalesAccount(),
                        product.getPurchaseAccount()))
                        .collect(Collectors.toList()));
                CreateProductsOutput_IP outputIP = productHandler.createProducts(inputIP);
                success = outputIP.getSuccess();
                updateProcessStatus(input, success ? "Done" : "Fail", outputIP.getMessage());
            } catch (Exception e) {
                errorMessage = e.getMessage();
                errorMessage = errorMessage.replace(";", ",");
                if (errorMessage.startsWith("Errors found in products data:")) {
                    String trimmedErrorMessage = errorMessage.substring("Errors found in products data:".length())
                            .trim();
                    errorList = trimmedErrorMessage.split("; ");
                    for (String error : errorList) {
                        errorMessages.add(error.trim());
                    }
                } else {
                    errorMessages.add(errorMessage);
                }
            }
        } catch (Exception e) {
            return new Output_CreateMultipleProductsIA(false, "Failed to process products: " + e.getMessage(), 400,
                    null, null);
        }
        return new Output_CreateMultipleProductsIA(success, "All products created successfully", success ? 200 : 207,
                errorMessages.isEmpty() ? null : errorMessages, null);
    }

    public String parseProductsStringToJson(String productsData) {
        try {
            System.out.println("Parsing products data to JSON: " + productsData);

            if (productsData == null || productsData.isEmpty()) {
                return "[]";
            }

            String[] rows = productsData.split("@#");
            if (rows.length < 2) {
                return "[]";
            }

            String[] headers = rows[0].split(",");
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{\"products\":");
            jsonBuilder.append("[");
            for (int i = 1; i < rows.length; i++) {
                String[] values = rows[i].split(",");

                for (int j = 0; j < values.length; j++) {
                    if (values[j] != null) {
                        values[j] = values[j].replace("&%", ",");
                    }
                }

                if (values.length != headers.length) {
                    System.err.println("Line " + (i + 1) + ": Column count mismatch. Expected " + headers.length
                            + " but found " + values.length);
                    continue;
                }

                if (i > 1) {
                    jsonBuilder.append(",");
                }

                jsonBuilder.append("{");

                for (int j = 0; j < headers.length; j++) {
                    String header = headers[j].trim();
                    String value = values[j].trim();

                    if (j > 0) {
                        jsonBuilder.append(",");
                    }

                    String jsonProperty = mapHeaderToJsonProperty(header);
                    jsonBuilder.append("\"").append(jsonProperty).append("\":");

                    jsonBuilder.append("\"").append(value).append("\"");
                }

                jsonBuilder.append("}");
            }
            jsonBuilder.append("]}");
            String jsonResult = jsonBuilder.toString();
            System.out.println("Generated JSON: " + jsonResult);
            return jsonResult;

        } catch (Exception e) {
            System.err.println("Error parsing products string to JSON: " + e.getMessage());
            return "[]";
        }
    }

    private String mapHeaderToJsonProperty(String header) {
        return switch (header.toLowerCase()) {
            case "salesaccount" ->
                "salesAccount";
            case "purchaseaccount" ->
                "purchaseAccount";
            default ->
                header.toLowerCase();
        };
    }

    private void updateProcessStatus(Input_CreateMultipleProductsIA request, String status, String message) {
        Request updateRequest = new Request();
        Request.ProcessDetail processDetail = new Request.ProcessDetail();

        processDetail.setInstanceId(request.getProcessInstanceId());
        processDetail.setStatus(status);
        processDetail.setMessage(message);

        updateRequest.setProcess(processDetail);
        port_UpdateScheduledProcessOP.handleUpdateScheduledProcess(updateRequest);
    }

}
