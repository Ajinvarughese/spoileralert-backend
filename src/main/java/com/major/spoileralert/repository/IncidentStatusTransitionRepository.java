package com.major.spoileralert.repository;

import com.major.spoileralert.entity.IncidentStatusTransition;
import com.major.spoileralert.enumeration.IncidentReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentStatusTransitionRepository extends JpaRepository<IncidentStatusTransition, Long> {

    List<IncidentStatusTransition> findByFromStatus(IncidentReportStatus fromStatus);

    List<IncidentStatusTransition> findByFromStatusAndIsDepAdminUpdatableTrue(IncidentReportStatus currentStatus);

    List<IncidentStatusTransition> findByFromStatusAndIsDepInchargeUpdatableTrue(IncidentReportStatus currentStatus);

}
