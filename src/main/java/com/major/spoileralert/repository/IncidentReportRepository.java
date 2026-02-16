package com.major.spoileralert.repository;

import com.major.spoileralert.entity.IncidentReport;
import com.major.spoileralert.enumeration.IncidentReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {

    List<IncidentReport> findByAssignedDepartmentIdOrderByCreatedDateDesc(Long departmentId);

    List<IncidentReport> findByReportedByIdOrderByCreatedDateDesc(Long id);

    List<IncidentReport> findByAssignedDepartmentIdAndStatusOrderByCreatedDateDesc(Long departmentId, IncidentReportStatus status);

    List<IncidentReport> findByReportedByIdAndStatusOrderByCreatedDateDesc(Long id, IncidentReportStatus status);

    List<IncidentReport> findByAssignedUserIdOrderByCreatedDateDesc(Long id);

    List<IncidentReport> findByAssignedUserIdAndStatusOrderByCreatedDateDesc(Long id, IncidentReportStatus status);

}