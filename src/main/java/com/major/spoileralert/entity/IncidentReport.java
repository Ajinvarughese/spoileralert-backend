package com.major.spoileralert.entity;

import com.major.spoileralert.enumeration.IncidentReportStatus;
import com.major.spoileralert.enumeration.Severity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "incident_report")
public class IncidentReport extends Audit {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 1000, nullable = false)
    private String description;

    @Column(name = "media_urls", length = 5000) // To store multiple URLs as a comma-separated string
    private String mediaUrls;

    @Column(name = "latitude") // Geolocation latitude
    private String latitude;

    @Column(name = "longitude") // Geolocation longitude
    private String longitude;

    @Column(name = "location", nullable = false)
    private String location;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private Severity severity;  // User will select this manually

    @Enumerated(EnumType.STRING) // Store the enum as a string in the database
    @Column(name = "status", nullable = false)
    private IncidentReportStatus status = IncidentReportStatus.REPORTED; // Default status

    @ManyToOne
    @JoinColumn(name = "assigned_department_id", nullable = false)
    private Department assignedDepartment;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @ManyToOne
    @JoinColumn(name = "reported_by")
    private User reportedBy;

}
