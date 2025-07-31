package com.techfoot.stockspree.OutboundPort.RedisPorts.Q_GetAllWorkspaces;

import java.util.List;
import java.util.Map;

public interface Port_GetAllWorkspacesOP {
    List<Map<String,String>> getAllWorkspaces();
}
