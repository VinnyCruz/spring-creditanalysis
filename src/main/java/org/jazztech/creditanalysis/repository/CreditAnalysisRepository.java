package org.jazztech.creditanalysis.repository;

import org.jazztech.creditanalysis.model.CreditAnalysis;
import org.jazztech.creditanalysis.repository.entity.CreditAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CreditAnalysisRepository extends JpaRepository<CreditAnalysisEntity, UUID> {
    List<CreditAnalysisEntity> findByClientId(UUID clientId);
}
