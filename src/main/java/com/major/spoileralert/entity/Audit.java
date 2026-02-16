package com.major.spoileralert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Timestamp;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "created_date", nullable = false, columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6)")
    @Temporal(TemporalType.TIMESTAMP) // Specify TemporalType.TIMESTAMP for Date
    @CreatedDate
    protected Timestamp createdDate; // Set default value to current time

    @Column(name = "modified_date", columnDefinition = "TIMESTAMP(6)")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    protected Timestamp modifiedDate;

}
