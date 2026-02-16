package com.major.spoileralert.enumeration;

/**
 * Enum representing the statuses of an Incident Report.
 */
public enum IncidentReportStatus {

    // Initial Reporting Stage
    REPORTED,             // Incident reported, pending review
    UNDER_REVIEW,         // Admin reviewing the report

    // Actionable Stages
    ASSIGNED,             // Assigned to in-charge
    IN_PROGRESS,          // Work actively being done
    ON_HOLD,              // Waiting for info, approval, etc.

    ESCALATED,            // Incident has been escalated

    // Completion
    RESOLVED,             // Work completed, awaiting confirmation
    VERIFIED,             // Verified as resolved by reviewer
    CLOSED,               // Fully closed after verification
    REJECTED,             // Invalid or not actionable

    // Optional Special Cases
    DUPLICATE,            // Marked as duplicate of another report
    CANCELLED             // Withdrawn or cancelled without action

}
