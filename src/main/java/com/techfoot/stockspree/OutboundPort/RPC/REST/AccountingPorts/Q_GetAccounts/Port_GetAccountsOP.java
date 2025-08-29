package com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts;

import org.springframework.stereotype.Service;

@Service    
public interface Port_GetAccountsOP {
    public Output_GetAccountsOP getAccounts(Input_GetAccountsOP input) throws Exception;
}
