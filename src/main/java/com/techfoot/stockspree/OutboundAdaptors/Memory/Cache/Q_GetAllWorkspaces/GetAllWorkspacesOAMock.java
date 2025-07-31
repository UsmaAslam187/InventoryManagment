package com.techfoot.stockspree.OutboundAdaptors.Memory.Cache.Q_GetAllWorkspaces;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techfoot.stockspree.OutboundPort.RedisPorts.Q_GetAllWorkspaces.*;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mock")
public class GetAllWorkspacesOAMock implements Port_GetAllWorkspacesOP {
    
    @Override
    public List<Map<String, String>> getAllWorkspaces() {
        String cachedData = "{ \"workspaces\": [" +
                   "{ \"workspace\": \"odoo\" }," +
                   "{ \"workspace\": \"oracle\" }" +
                   "] }";
        List<Map<String, String>> workspaceList = null;
        Map<String, List<Map<String, String>>> result = deserializeWorkspaceData(cachedData);

        if (result != null) {
            workspaceList = result.get("workspaces");
            System.out.println("Fetched workspaces from cache........");
            System.out.println(workspaceList);
        }

        return workspaceList;
    }

    private Map<String, List<Map<String, String>>> deserializeWorkspaceData(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, new TypeReference<Map<String, List<Map<String, String>>>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
