package org.jazztech.creditanalysis.service.create;

import java.math.BigDecimal;
import java.util.UUID;
import org.jazztech.creditanalysis.controller.request.CreditAnalysisRequest;
import org.jazztech.creditanalysis.mapper.CreditEntityMapper;
import org.jazztech.creditanalysis.mapper.CreditEntityMapperImpl;
import org.jazztech.creditanalysis.repository.entity.CreditAnalysisEntity;
import org.jazztech.creditanalysis.service.search.SearchAnalysisService;
import org.mockito.Spy;

public class Factory {
    @Spy
    CreditEntityMapper creditEntityMapper = new CreditEntityMapperImpl();

    public CreditAnalysisRequest requestFactory() {
        return CreditAnalysisRequest.builder()
                .clientId(UUID.randomUUID())
                .monthlyIncome(new BigDecimal(20000))
                .requestedAmount(new BigDecimal(1000))
                .build();
    }
}
