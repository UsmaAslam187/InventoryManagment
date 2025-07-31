package com.techfoot.stockspree.Business.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Error_DomainEntity {
    Boolean success;
    String message;

    public Error_DomainEntity(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}