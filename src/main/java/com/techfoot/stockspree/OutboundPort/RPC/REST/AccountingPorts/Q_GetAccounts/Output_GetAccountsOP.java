package com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts;

import lombok.*;

import com.fasterxml.jackson.databind.JsonNode;

@Data
@AllArgsConstructor
public class Output_GetAccountsOP {
    private Boolean success;
    private String message;
    private JsonNode data;
}
