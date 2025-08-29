package com.techfoot.stockspree.OutboundAdaptors.REST.Accounting.Q_GetAccounts.Accounting_Contracts;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private String workspace;
    private String email;
    private Integer accountId;
}
