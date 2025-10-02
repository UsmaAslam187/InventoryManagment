package com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Tables;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class IdentifyingTable {

    @Version
    @Column(name = "aggregateVersion")
    private Long aggregateVersion;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column(name = "createdBy", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "modifiedAt")
    private LocalDate modifiedAt;

    @Column(name = "modifiedBy")
    private String modifiedBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDate.now();
    }
}
