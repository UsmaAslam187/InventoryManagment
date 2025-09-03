package com.techfoot.stockspree.InboundAdaptors.REST.Database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.techfoot.stockspree.InboundPort.CreateDatabase.C_CreateDatabaseHandler;

@RestController
public class Adaptor_CreateDbIA {
    @Autowired
    private C_CreateDatabaseHandler createDatabaseHandler;

       @PostMapping("stockspree/system/create-db")
      public ResponseEntity<Output_CreateDbIA> createTenantDatabase(@RequestBody Input_CreateDbIA tenantRequest) {

        Output_CreateDbIA response = createDatabaseHandler.createDb(tenantRequest);
        if (response.getSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
