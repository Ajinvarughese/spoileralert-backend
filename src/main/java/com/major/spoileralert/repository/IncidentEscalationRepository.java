package com.major.spoileralert.repository;

import com.major.spoileralert.entity.IncidentEscalation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentEscalationRepository extends JpaRepository<IncidentEscalation, Long> {
}
