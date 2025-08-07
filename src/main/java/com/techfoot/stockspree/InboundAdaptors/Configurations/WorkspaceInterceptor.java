package com.techfoot.stockspree.InboundAdaptors.Configurations;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

@Component
public class WorkspaceInterceptor implements HandlerInterceptor {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        CustomHttpRequestWrapper wrappedRequest = new CustomHttpRequestWrapper(request);
        String requestBody = wrappedRequest.getBody();
        System.out.println("Request Body: " + requestBody);
        System.out.println("Request URL: " + request.getRequestURL());
        System.out.println("Request Method: " + request.getMethod());
        
        if ("POST".equalsIgnoreCase(wrappedRequest.getMethod())) {
            if (requestBody != null && !requestBody.isEmpty()) {
                try {
                    Map<String, Object> requestBodyMap = objectMapper.readValue(requestBody, Map.class);
                    String workspace = (String) requestBodyMap.get("workspace");
                    
                    // Check if workspace is in the root level
                    if (workspace == null && requestBodyMap.get("data") instanceof Map) {
                        workspace = (String) ((Map<?, ?>) requestBodyMap.get("data")).get("workspace");
                    }
                    
                    // For stockspree endpoints, try to extract workspace from different possible locations
                    if (workspace == null && request.getRequestURL().toString().contains("/stockspree/")) {
                        // Try to get workspace from headers
                        workspace = request.getHeader("X-Workspace");
                        if (workspace == null) {
                            // Use a default workspace for stockspree
                            workspace = "default";
                        }
                    }

                    if (workspace != null) {
                        // Set the workspace context, which will be used in fully qualified table names
                        WorkspaceContext.setCurrentWorkspace(workspace);
                        System.out.println("Workspace: " + WorkspaceContext.getCurrentWorkspace());
                    } else {
                        System.out.println("No workspace found in request");
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing request body: " + e.getMessage());
                    // Don't fail the request, just log the error
                    // response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    // response.getWriter().write("Invalid request body");
                    // return false;
                }
            }
        }

        request.setAttribute("wrappedRequest", wrappedRequest);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Clean up if necessary
        WorkspaceContext.clear();
    }
}
