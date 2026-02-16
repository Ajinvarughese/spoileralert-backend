package com.major.spoileralert.entity;

import com.major.spoileralert.enumeration.IncidentReportStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "incident_status_transition")
public class IncidentStatusTransition extends Audit {

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", nullable = false)
    private IncidentReportStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false)
    private IncidentReportStatus toStatus;

    @Column(name = "is_dep_admin_updatable", nullable = false)
    private boolean isDepAdminUpdatable = false;

    @Column(name = "is_dep_incharge_updatable", nullable = false)
    private boolean isDepInchargeUpdatable = false;

}
