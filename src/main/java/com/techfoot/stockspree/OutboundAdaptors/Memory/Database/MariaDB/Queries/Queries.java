package com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Queries;

import org.springframework.stereotype.Service;

@Service
public class Queries {
    public String getInsertProductQuery(String database) {
        return "INSERT INTO " + database + ".Product " +
                "(name, description, price) VALUES " +
                "(:name, :description, :price)";
    }

    public String getSelectProductIdByNameQuery(String database) {
        return "SELECT id FROM " + database + ".Product WHERE name = :name";
    }
}
