package com.major.spoileralert.dto;

import com.major.spoileralert.enumeration.IncidentReportStatus;
import lombok.Data;

@Data
public class UpdateIncidentStatusRequestDto {

    private IncidentReportStatus status;

    private Long assignedToId;

    private String escalationReason;

    private Long initiatedById;

}
