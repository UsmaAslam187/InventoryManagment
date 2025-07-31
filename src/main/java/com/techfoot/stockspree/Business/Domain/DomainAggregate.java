package com.techfoot.stockspree.Business.Domain;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class DomainAggregate {

    private LocalDate createdAt;
    private String createdBy;
    private LocalDate modifiedAt;
    private String modifiedBy;
    private Long aggregateVersion;
}
