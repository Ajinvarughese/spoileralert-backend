package com.major.spoileralert.dto;

import com.major.spoileralert.enumeration.IncidentReportStatus;
import lombok.Data;

import java.util.Map;

@Data
public class DepartmentIncidentAnalyticsDto {

    private String departmentName;

    private Long totalIncidents;

    private Map<IncidentReportStatus, Long> statusCounts;

}
