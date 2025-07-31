package com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Tables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "product")
@Getter
@Setter
@ToString
public class Product_Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Column(name = "price")
    private Double price;

    @Column(name = "tax", length = 100)
    private String tax;

    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "sales_account")
    private Integer salesAccount;

    @Column(name = "purchase_account")
    private Integer purchaseAccount;
}
