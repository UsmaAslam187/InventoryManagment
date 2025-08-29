package com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Input_GetAccountsOP {
    private Integer accountID;
    // private Integer parentID;
}
