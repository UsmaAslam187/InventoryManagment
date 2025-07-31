package com.techfoot.stockspree.OutboundAdaptors.Memory.Cache.Q_GetAllWorkspaces;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techfoot.stockspree.OutboundAdaptors.Memory.Cache.Configurations.*;
import com.techfoot.stockspree.OutboundPort.RedisPorts.Q_GetAllWorkspaces.*;

@Service
@Profile("prod")
public class GetAllWorkspacesOAProd implements Port_GetAllWorkspacesOP {

    private RedisClient redisClient;

    public GetAllWorkspacesOAProd() {
        this.redisClient = new RedisClient();
    }

    @Override
    public List<Map<String, String>> getAllWorkspaces() {
        String cacheKey = "WORKSPACES";
        String cachedData = redisClient.get(cacheKey);
        List<Map<String, String>> workspaceList = null;
        Map<String, List<Map<String, String>>> result = deserializeWorkspaceData(cachedData);

        if (result != null) {
            workspaceList = result.get("workspaces");
            System.out.println("Fetched workspaces from cache.");
            System.out.println(workspaceList);
        }
        return workspaceList;
    }
    private Map<String, List<Map<String, String>>> deserializeWorkspaceData(String cachedData) {
        try {
            return new ObjectMapper().readValue(cachedData, new TypeReference<Map<String, List<Map<String, String>>>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
