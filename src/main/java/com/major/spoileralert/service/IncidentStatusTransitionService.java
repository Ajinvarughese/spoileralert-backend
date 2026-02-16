package com.major.spoileralert.service;

import com.major.spoileralert.entity.IncidentStatusTransition;
import com.major.spoileralert.enumeration.IncidentReportStatus;
import com.major.spoileralert.repository.IncidentStatusTransitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IncidentStatusTransitionService {

    @Autowired
    IncidentStatusTransitionRepository incidentStatusTransitionRepository;

    /**
     * Retrieves all valid incident status transitions from the given current status,
     * based on the role (Admin or In-Charge).
     *
     * @param currentStatus the current status of the incident
     * @param isDepartmentAdmin true if the requester is a department admin, false if In-Charge
     * @return list of valid transitions for the role from the current status
     */
    public List<IncidentStatusTransition> getValidTransitionsByRole(IncidentReportStatus currentStatus, boolean isDepartmentAdmin) {
        // If user is admin, fetch transitions where admin is allowed to perform update
        if (isDepartmentAdmin) {
            return incidentStatusTransitionRepository.findByFromStatusAndIsDepAdminUpdatableTrue(currentStatus);
        } else {
            // If user is in-charge, fetch transitions allowed for in-charge
            return incidentStatusTransitionRepository.findByFromStatusAndIsDepInchargeUpdatableTrue(currentStatus);
        }
    }

    /**
     * Validates whether transitioning from the current status to the next status
     * is allowed for the given role (Admin or In-Charge).
     *
     * @param current the current status of the incident
     * @param next the desired next status of the incident
     * @param isDepartmentAdmin true if the user is a department admin, false if In-Charge
     * @return true if the transition is valid, false otherwise
     */
    public boolean isValidTransition(IncidentReportStatus current, IncidentReportStatus next, boolean isDepartmentAdmin) {
        // Fetch the valid transitions based on user role and current status
        List<IncidentStatusTransition> validTransitions = getValidTransitionsByRole(current, isDepartmentAdmin);

        // Check if the next status exists in the list of valid transitions
        return validTransitions.stream()
                .anyMatch(transition -> transition.getToStatus() == next);
    }

    /**
     * Returns a list of possible next statuses (toStatus) from the given current status
     * based on whether the user is an admin or in-charge.
     *
     * @param fromStatus the current status of the incident
     * @param isDepartmentAdmin true if the user is a department admin, false if In-Charge
     * @return list of allowed next statuses
     */
    public List<IncidentReportStatus> getAllowedNextStatuses(IncidentReportStatus fromStatus, boolean isDepartmentAdmin) {
        // Get all valid transitions based on role
        List<IncidentStatusTransition> transitions = getValidTransitionsByRole(fromStatus, isDepartmentAdmin);

        // Extract and return only the 'toStatus' values
        return transitions.stream()
                .map(IncidentStatusTransition::getToStatus)
                .collect(Collectors.toList());
    }

}
