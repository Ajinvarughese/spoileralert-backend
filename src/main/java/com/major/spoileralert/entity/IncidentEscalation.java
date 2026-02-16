package com.major.spoileralert.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "incident_escalation")
public class IncidentEscalation extends Audit {

    @ManyToOne
    @JoinColumn(name = "incident_report_id", nullable = false)
    private IncidentReport incidentReport; // Reference to the IncidentReport entity

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser; // Reference to the previous assignee (nullable)

    @Column(name = "escalation_reason", nullable = false)
    private String escalationReason; // Reason for the escalation

    @ManyToOne
    @JoinColumn(name = "initiated_by_id")
    private User initiatedBy; // Reference to the user who initiated the escalation (nullable)

}
