package com.major.spoileralert.service;

import com.major.spoileralert.dto.DepartmentIncidentAnalyticsDto;
import com.major.spoileralert.dto.IncidentDto;
import com.major.spoileralert.dto.UpdateIncidentStatusRequestDto;
import com.major.spoileralert.entity.*;
import com.major.spoileralert.entity.*;
import com.major.spoileralert.enumeration.IncidentReportStatus;
import com.major.spoileralert.exception.EntityNotFoundException;
import com.major.spoileralert.repository.IncidentReportRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class IncidentReportService {

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    SubcategoryDepartmentService subcategoryDepartmentService;

    @Autowired
    IncidentReportRepository incidentReportRepository;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    EmailService emailService; // Used to send emails

    @Autowired
    AuthService authService;

    @Autowired
    IncidentEscalationService incidentEscalationService;

    @Autowired
    IncidentStatusTransitionService incidentStatusTransitionService;

    public IncidentReport createIncident(IncidentDto incidentDto) throws EntityNotFoundException, MessagingException {
        log.info("Saving new incidentReport for user: {}", incidentDto.getReportedBy());

        // Upload files and get URLs
        String mediaUrls = uploadFiles(incidentDto.getFiles());

        // Fetch assigned department (Ensuring it's present)
        SubcategoryDepartment subcategoryDepartment = subcategoryDepartmentService.getSubcategoryDepartmentBySubcategory(incidentDto.getSubcategory());

        if (subcategoryDepartment == null) {
            log.error("No department assigned for subcategory: {}", incidentDto.getSubcategory());
            throw new EntityNotFoundException("No department assigned for subcategory: " + incidentDto.getSubcategory());
        }

        // Create and save IncidentReport entity
        IncidentReport incidentReport = buildIncidentEntity(incidentDto, mediaUrls, subcategoryDepartment.getDepartment());
        IncidentReport savedIncidentReport = saveIncidentReport(incidentReport);

        // Send email to department admins about the new incident
        sendMailToDepartmentAdmin(subcategoryDepartment.getDepartment().getId(), savedIncidentReport);

        log.info("IncidentReport saved successfully with ID: {}", savedIncidentReport.getId());
        return savedIncidentReport;
    }

    // Method to send email to department admins
    private void sendMailToDepartmentAdmin(Long departmentId, IncidentReport incidentReport) throws MessagingException {
        // Step 1: Fetch department admin email
        String departmentAdminEmail = authService.getDepartmentAdminEmail(departmentId);
        if (departmentAdminEmail != null) {
            // Step 2: Compose email content
            String subject = "🚨 New Incident Reported - Immediate Attention Required";

            String body = "<html>" + "<body style='font-family: Arial, sans-serif;'>" + "<h2>Dear Department Admin,</h2>" + "<p>A new incident has been reported under your department. Please find the details below:</p>" + "<table style='border-collapse: collapse;'>" + "<tr><td style='padding: 8px;'><strong>📌 Title:</strong></td><td style='padding: 8px;'>" + incidentReport.getTitle() + "</td></tr>" + "<tr><td style='padding: 8px;'><strong>📝 Description:</strong></td><td style='padding: 8px;'>" + incidentReport.getDescription() + "</td></tr>" + "<tr><td style='padding: 8px;'><strong>📍 Location:</strong></td><td style='padding: 8px;'>" + incidentReport.getLocation() + "</td></tr>" + "</table>" + "<p>Please take the necessary action at your earliest convenience.</p>" + "<p>" + "<a href='http://localhost:3000' " + "style='background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center; " + "text-decoration: none; display: inline-block; border-radius: 5px;'>" + "📂 Open Spoiler Alert App</a>" + "</p>" + "<p>If the button above does not work, you can visit: <a href='http://localhost:3000'>http://localhost:3000</a></p>" + "<p>Thank you for your attention.</p>" + "<p>Best regards,<br><strong>Spoiler Alert Team</strong></p>" + "</body>" + "</html>";

            // Step 3: Send the email
            emailService.sendEmail(departmentAdminEmail, subject, body);
        }
    }

    public IncidentReport saveIncidentReport(IncidentReport incidentReport) {
        return incidentReportRepository.save(incidentReport);
    }

    private String uploadFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return ""; // No files to upload
        }

        return files.stream().map(file -> {
                    try {
                        return fileUploadService.uploadFile(file); // Expected to return a String URL
                    } catch (IOException e) {
                        log.error("File upload failed for: {}", file.getOriginalFilename(), e);
                        return ""; // Return an empty string to prevent stream failure
                    }
                }).filter(url -> !url.isEmpty()) // Remove empty entries from failed uploads
                .collect(Collectors.joining(",")); // Store as a comma-separated string
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty() || file.getSize() == 0) {
            throw new IllegalArgumentException("Uploaded file cannot be empty.");
        }
        String fileType = Optional.ofNullable(file.getContentType()).orElse("");
        if (!fileType.startsWith("image/") && !fileType.startsWith("video/")) {
            throw new IllegalArgumentException("Only image and video files are allowed.");
        }
    }

    private IncidentReport buildIncidentEntity(IncidentDto incidentDto, String mediaUrls, Department department) {
        IncidentReport incidentReport = new IncidentReport();
        incidentReport.setTitle(incidentDto.getTitle());
        incidentReport.setDescription(incidentDto.getDescription());
        incidentReport.setSubcategory(incidentDto.getSubcategory());
        incidentReport.setLatitude(incidentDto.getLatitude());
        incidentReport.setLongitude(incidentDto.getLongitude());
        incidentReport.setLocation(incidentDto.getLocation());
        incidentReport.setMediaUrls(mediaUrls);
        incidentReport.setSeverity(incidentDto.getSeverity());
        incidentReport.setAssignedDepartment(department);
        incidentReport.setReportedBy(incidentDto.getReportedBy());
        return incidentReport;
    }

    public IncidentReport getIncidentById(Long id) throws EntityNotFoundException {
        return incidentReportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("IncidentReport not found"));
    }

    public List<IncidentReport> getAllIncidentsByDepartmentId(Long departmentId) {
        return incidentReportRepository.findByAssignedDepartmentIdOrderByCreatedDateDesc(departmentId);
    }

    public List<IncidentReport> getAllIncidentsByReportedById(Long id) {
        return incidentReportRepository.findByReportedByIdOrderByCreatedDateDesc(id);
    }

    public List<IncidentReport> getAllIncidentsByAssignedUserId(Long id) {
        return incidentReportRepository.findByAssignedUserIdOrderByCreatedDateDesc(id);
    }

    public List<IncidentReport> getAllIncidentsByDepartmentIdAndStatus(Long departmentId, IncidentReportStatus status) {
        return incidentReportRepository.findByAssignedDepartmentIdAndStatusOrderByCreatedDateDesc(departmentId, status);
    }

    public List<IncidentReport> getAllIncidentsByReportedByIdAndStatus(Long id, IncidentReportStatus status) {
        return incidentReportRepository.findByReportedByIdAndStatusOrderByCreatedDateDesc(id, status);
    }

    public List<IncidentReport> getAllIncidentsByAssignedUserIdAndStatus(Long id, IncidentReportStatus status) {
        return incidentReportRepository.findByAssignedUserIdAndStatusOrderByCreatedDateDesc(id, status);
    }

    /*public String updateIncidentStatus(Long id, IncidentReportStatus next) throws EntityNotFoundException {
        IncidentReport incidentReport = getIncidentById(id);

        IncidentReportStatus current = incidentReport.getStatus();

        if (!incidentStatusTransitionService.isValidTransition(current, next)) {
            throw new IllegalStateException("Cannot transition from " + current + " to " + next);
        }

        incidentReport.setStatus(next);
        saveIncidentReport(incidentReport);
        return "Status updated successfully";
    }*/

    public String updateIncidentStatusAndAssign(Long incidentId, UpdateIncidentStatusRequestDto request, Boolean isDepartmentAdmin) throws EntityNotFoundException {

        IncidentReportStatus nextStatus = request.getStatus();
        Long assignedToId = request.getAssignedToId();
        String escalationReason = request.getEscalationReason();
        Long initiatedById = request.getInitiatedById();

        // Fetch incident report
        IncidentReport incidentReport = getIncidentById(incidentId);
        IncidentReportStatus currentStatus = incidentReport.getStatus();

        // Validate transition using the TransitionService
        if (!incidentStatusTransitionService.isValidTransition(currentStatus, nextStatus, isDepartmentAdmin)) {
            throw new IllegalStateException("Cannot transition from " + currentStatus + " to " + nextStatus);
        }

        // Handle status transition logic
        if (nextStatus == IncidentReportStatus.ASSIGNED) {
            // Assign user when status changes to ASSIGNED
            assignUserToIncident(incidentReport, assignedToId);
        } else if (nextStatus == IncidentReportStatus.ESCALATED) {
            // Create escalation entry when status changes to ESCALATED
            createEscalationEntry(incidentReport, incidentReport.getAssignedUser(), escalationReason, initiatedById);
        }

        // Update status
        incidentReport.setStatus(nextStatus);
        saveIncidentReport(incidentReport);

        return "Incident status updated successfully";
    }

    /**
     * Assign User to Incident
     */
    private void assignUserToIncident(IncidentReport incidentReport, Long assignedToId) throws EntityNotFoundException {
        if (assignedToId == null) {
            throw new IllegalArgumentException("AssignedTo user ID must be provided.");
        }
        User assignee = authService.getUserById(assignedToId);
        incidentReport.setAssignedUser(assignee);
    }

    /**
     * Create Escalation Entry
     */
    private void createEscalationEntry(IncidentReport incidentReport, User oldAssignee, String escalationReason, Long initiatedById) throws EntityNotFoundException {
        if (escalationReason == null || escalationReason.isBlank()) {
            throw new IllegalArgumentException("Escalation reason must be provided.");
        }

        IncidentEscalation escalation = new IncidentEscalation();
        escalation.setIncidentReport(incidentReport);
        escalation.setFromUser(oldAssignee); // current assignee (maybe null)
        escalation.setEscalationReason(escalationReason);
        escalation.setInitiatedBy(initiatedById != null ? authService.getUserById(initiatedById) : null);

        incidentEscalationService.createIncidentEscalation(escalation);
    }

    /**
     * Returns analytics per department with dynamic status-wise incident counts.
     */
    public List<DepartmentIncidentAnalyticsDto> getDepartmentWiseAnalytics() {
        List<Department> departments = departmentService.getAllDepartments();

        return departments.stream().map(department -> {
            // Fetch incident reports for the department
            List<IncidentReport> incidentReports =
                    Optional.ofNullable(incidentReportRepository
                                    .findByAssignedDepartmentIdOrderByCreatedDateDesc(department.getId()))
                            .orElse(Collections.emptyList());

            long totalIncidents = incidentReports.size();

            // Initialize all statuses with zero count
            Map<IncidentReportStatus, Long> statusCounts = Arrays.stream(IncidentReportStatus.values())
                    .collect(Collectors.toMap(status -> status, status -> 0L));

            // Count actual status occurrences
            Map<IncidentReportStatus, Long> actualCounts = incidentReports.stream()
                    .filter(report -> report.getStatus() != null)
                    .collect(Collectors.groupingBy(IncidentReport::getStatus, Collectors.counting()));

            // Merge actual counts into initialized map
            actualCounts.forEach((status, count) -> statusCounts.merge(status, count, Long::sum));

            // Prepare and return DTO
            DepartmentIncidentAnalyticsDto dto = new DepartmentIncidentAnalyticsDto();
            dto.setDepartmentName(department.getName());
            dto.setTotalIncidents(totalIncidents);
            dto.setStatusCounts(statusCounts);

            return dto;
        }).collect(Collectors.toList());
    }

}
