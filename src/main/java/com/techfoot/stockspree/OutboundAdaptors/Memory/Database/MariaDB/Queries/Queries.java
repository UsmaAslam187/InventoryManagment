package com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Queries;

import org.springframework.stereotype.Service;

@Service
public class Queries {
    public String getInsertProductQuery(String database) {
        return "INSERT INTO " + database + ".Product " +
                "(name, code, price, tax, type, sales_account, purchase_account) VALUES " +
                "(:name, :code, :price, :tax, :type, :salesAccount, :purchaseAccount)";
    }

    public String getSelectProductIdByNameQuery(String database, String name) {
        return "SELECT id FROM " + database + ".Product WHERE name = :name";
    }
}
