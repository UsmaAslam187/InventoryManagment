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

    public String getCountSpecificProductsQuery(String db, String searchedValue, Boolean nameFlag) {
        if (!nameFlag) {
            return "SELECT COUNT(*) as count FROM " + db + ".Product where name like '%" + searchedValue
                    + "%' or code like '%"
                    + searchedValue + "%' or price like '%" + searchedValue + "%' or tax like '%" + searchedValue
                    + "%' or type like '%" + searchedValue + "%' or sales_account like '%" + searchedValue
                    + "%' or purchase_account like '%" + searchedValue + "%'";
        } else {
            return "SELECT COUNT(*) as count FROM " + db + ".Product where code like '%" + searchedValue + "%' or name like '%" + searchedValue + "%'";
        }
    }
    
    public String getSpecificProductsQuery(String db, String searchedValue, Boolean nameFlag) {
        if (!nameFlag) {
            return "SELECT * FROM " + db + ".Product where name like '%" + searchedValue
                    + "%' or code like '%"
                    + searchedValue + "%' or price like '%" + searchedValue + "%' or tax like '%" + searchedValue
                    + "%' or type like '%" + searchedValue + "%' or sales_account like '%" + searchedValue
                    + "%' or purchase_account like '%" + searchedValue + "%'";
        } else {
            return "SELECT *FROM " + db + ".Product where code like '%" + searchedValue + "%' or name like '%" + searchedValue + "%'";
        }
    }

    public String getSelectAllProductsQuery(String db, int start, int end) {
        return "SELECT * FROM " + db + ".Product limit " + start + ", " + end + "";
    }

    public String getAllProductsCount(String db) {
        return "select count(*) as count from " + db + ".Product";
    }

    public String getSelectProductByCodeOrNameQuery(String db, String code, String name) {
        return "SELECT * FROM " + db + ".Product where code = :code or name = :name";
    }

}
