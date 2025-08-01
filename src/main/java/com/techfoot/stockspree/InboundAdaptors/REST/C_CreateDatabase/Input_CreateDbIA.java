package com.techfoot.stockspree.InboundAdaptors.REST.C_CreateDatabase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
@Getter
public class Input_CreateDbIA {
    private String subdomain;
    private String tenantName;
    private String tenantEmail;
    private Integer tenantID;

    public String getDBName() { return "techfoot_stockspree_db_" + subdomain; }
}
