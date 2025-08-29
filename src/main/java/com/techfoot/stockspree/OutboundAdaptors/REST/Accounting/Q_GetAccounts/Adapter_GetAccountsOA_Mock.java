package com.techfoot.stockspree.OutboundAdaptors.REST.Accounting.Q_GetAccounts;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts.Input_GetAccountsOP;
import com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts.Output_GetAccountsOP;
import com.techfoot.stockspree.OutboundPort.RPC.REST.AccountingPorts.Q_GetAccounts.Port_GetAccountsOP;

@Service
@Profile("mock")
public class Adapter_GetAccountsOA_Mock implements Port_GetAccountsOP {
    public Output_GetAccountsOP getAccounts(Input_GetAccountsOP input) throws Exception {
        if(input.getAccountID() == null) {
            return new Output_GetAccountsOP(false, "AccountID is required", null);
        }
      return new Output_GetAccountsOP(true, "Accounts fetched successfully",  null);
    }
}
