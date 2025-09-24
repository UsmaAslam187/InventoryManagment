package com.techfoot.stockspree.OutboundAdaptors.REST.Accounting.Q_GetAccounts.Accounting_Contracts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private String workspace;
    private String email;
    private Integer accountID;
}
