package com.major.spoileralert.controller;

import com.major.spoileralert.dto.IncidentDto;
import com.major.spoileralert.dto.UpdateIncidentStatusRequestDto;
import com.major.spoileralert.entity.IncidentReport;
import com.major.spoileralert.enumeration.IncidentReportStatus;
import com.major.spoileralert.exception.EntityNotFoundException;
import com.major.spoileralert.service.IncidentReportService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.major.spoileralert.dto.DepartmentIncidentAnalyticsDto;

import java.util.List;

@RestController
@Slf4j
public class IncidentReportController {

    @Autowired
    private IncidentReportService incidentReportService;

    @PostMapping(value = "/incident-reports", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> submitIncident(@ModelAttribute IncidentDto incidentDto) throws EntityNotFoundException, MessagingException {
        IncidentReport savedIncidentReport = incidentReportService.createIncident(incidentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedIncidentReport);
    }

    @GetMapping(value = "/incident-reports/{id}")
    public IncidentReport getIncidentById(@PathVariable Long id) throws EntityNotFoundException {
        return incidentReportService.getIncidentById(id);
    }

    @GetMapping(value = "/departments/{id}/incident-reports")
    public List<IncidentReport> getAllIncidentsByDepartmentId(@PathVariable Long id, @RequestParam(value = "status", required = false) IncidentReportStatus status) {
        if (status == null) {
            return incidentReportService.getAllIncidentsByDepartmentId(id);
        } else {
            return incidentReportService.getAllIncidentsByDepartmentIdAndStatus(id, status);
        }
    }

    @GetMapping(value = "/reported-by/{id}/incident-reports")
    public List<IncidentReport> getAllIncidentsByReportedById(@PathVariable Long id, @RequestParam(value = "status", required = false) IncidentReportStatus status) {
        if (status == null) {
            return incidentReportService.getAllIncidentsByReportedById(id);
        } else {
            return incidentReportService.getAllIncidentsByReportedByIdAndStatus(id, status);
        }
    }

    @GetMapping(value = "/assigned-user/{id}/incident-reports")
    public List<IncidentReport> getAllIncidentsByAssignedUserId(@PathVariable Long id, @RequestParam(value = "status", required = false) IncidentReportStatus status) {
        if (status == null) {
            return incidentReportService.getAllIncidentsByAssignedUserId(id);
        } else {
            return incidentReportService.getAllIncidentsByAssignedUserIdAndStatus(id, status);
        }
    }

    /*@PutMapping("/incident-reports/{id}")
    public ResponseEntity<String> updateIncidentStatus(@PathVariable Long id, @RequestParam(value = "status") IncidentReportStatus status) throws EntityNotFoundException {
        return ResponseEntity.ok(incidentReportService.updateIncidentStatus(id, status));
    }*/

    @PutMapping("/incident-reports/{id}/update-status")
    public ResponseEntity<String> updateIncidentStatusAndAssign(
            @PathVariable Long id,
            @RequestBody UpdateIncidentStatusRequestDto request,
            @RequestParam Boolean isDepartmentAdmin
    ) throws EntityNotFoundException {
        String response = incidentReportService.updateIncidentStatusAndAssign(
                id,
                request,
                isDepartmentAdmin
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/incident-reports/department-wise")
    public ResponseEntity<List<DepartmentIncidentAnalyticsDto>> getDepartmentWiseAnalytics() {
        List<DepartmentIncidentAnalyticsDto> departmentAnalytics = incidentReportService.getDepartmentWiseAnalytics();
        return ResponseEntity.ok(departmentAnalytics);
    }

}
