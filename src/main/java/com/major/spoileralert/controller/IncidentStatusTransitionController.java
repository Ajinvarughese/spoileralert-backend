package com.major.spoileralert.controller;

import com.major.spoileralert.enumeration.IncidentReportStatus;
import com.major.spoileralert.service.IncidentStatusTransitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IncidentStatusTransitionController {

    @Autowired
    IncidentStatusTransitionService incidentStatusTransitionService;

    /**
     * API to fetch possible next statuses based on current status and user role.
     *
     * @param fromStatus the current status of the incident
     * @param isDepartmentAdmin true if the user is a department admin; false for in-charge
     * @return list of possible next statuses
     */
    @GetMapping("/incidents/status-transitions/next")
    public ResponseEntity<List<IncidentReportStatus>> getNextStatuses(
            @RequestParam IncidentReportStatus fromStatus,
            @RequestParam Boolean isDepartmentAdmin) {

        List<IncidentReportStatus> nextStatuses = incidentStatusTransitionService
                .getAllowedNextStatuses(fromStatus, isDepartmentAdmin);

        return ResponseEntity.ok(nextStatuses);
    }

}
