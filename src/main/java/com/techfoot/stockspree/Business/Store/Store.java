package com.techfoot.stockspree.Business.Store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Store {
    @Autowired
    public Products product;
}
