package com.major.spoileralert.service;

import com.major.spoileralert.entity.IncidentEscalation;
import com.major.spoileralert.repository.IncidentEscalationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IncidentEscalationService {

    @Autowired
    IncidentEscalationRepository incidentEscalationRepository;

    public void createIncidentEscalation(IncidentEscalation escalation) {
        incidentEscalationRepository.save(escalation);
    }

}
