package com.phyriak.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base_seq")
    @SequenceGenerator(
            name = "base_seq",
            sequenceName = "base_seq",
            //for h2 base can be removed, when you keep it you have to define init base in db in database config
       //     schema = "base",
            allocationSize = 50)
    private Long id;
    @Version
    @Column(nullable = false)
    private Long version;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false, updatable = false)
    private String createdBy;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Column(nullable = false)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        createdBy = "SYSTEM";
        updatedAt = LocalDateTime.now();
        updatedBy = "SYSTEM";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updatedBy = "SYSTEM";
    }
}
