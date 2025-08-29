package com.techfoot.stockspree.OutboundAdaptors.REST.Accounting.Q_GetAccounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techfoot.stockspree.InboundAdaptors.Configurations.WorkspaceContext;
import com.techfoot.stockspree.OutboundAdaptors.Configurations.TechfootServicesConfig;
import com.techfoot.stockspree.OutboundAdaptors.REST.Accounting.Q_GetAccounts.Accounting_Contracts.Request;
import com.techfoot.stockspree.OutboundAdaptors.REST.Accounting.Q_GetAccounts.Accounting_Contracts.Response;
import com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts.Input_GetAccountsOP;
import com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts.Output_GetAccountsOP;
import com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts.Port_GetAccountsOP;

@Service
@Profile("prod")
public class Adapter_GetAccountsOA_Prod implements Port_GetAccountsOP{
    private TechfootServicesConfig.AccountingService service;

    @Autowired
    private ObjectMapper objectMapper;

    public Adapter_GetAccountsOA_Prod() {
        service = new TechfootServicesConfig().accountingService();
    }
    private final RestTemplate restTemplate = new RestTemplate();

    public Output_GetAccountsOP getAccounts(Input_GetAccountsOP input) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Request req = new Request(WorkspaceContext.getCurrentWorkspace(), "", input.getAccountID());

        HttpEntity<Request> entity = new HttpEntity<>(req, headers);
        String url = String.format("http://%s:%d/accspree/COA/get-accounts", service.getHostName(), service.getPortNumber());

        Response response = new Response();
        try {
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(url, entity, String.class);
            try {
                response = objectMapper.readValue(apiResponse.getBody(), Response.class);
            } catch (Exception jsonException) {
                response = new Response(false, "Failed to parse API response: " + jsonException.getMessage(), 
                    apiResponse.getStatusCodeValue(), null, apiResponse.getBody());
            }
        } catch (HttpClientErrorException e) {
            try {
                response = objectMapper.readValue(e.getResponseBodyAsString(), Response.class);
            } catch (Exception jsonException) {
                response = new Response(false, e.getMessage(), e.getStatusCode().value(), null, e.getResponseBodyAsString());
            }
        } catch (Exception e) {
            response = new Response(false, e.getMessage(), 500, null, e.getMessage());
        }

        if(response.isSuccess()){
         return new Output_GetAccountsOP(true, response.getMessage(), response.getData());
        } else {
            return new Output_GetAccountsOP(false, response.getMessage(), null);
        }
    }
}

