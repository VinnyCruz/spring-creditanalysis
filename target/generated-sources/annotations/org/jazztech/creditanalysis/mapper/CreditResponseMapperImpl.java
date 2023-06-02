package org.jazztech.creditanalysis.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.jazztech.creditanalysis.controller.response.CreditAnalysisResponse;
import org.jazztech.creditanalysis.repository.entity.CreditAnalysisEntity;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-02T04:03:20-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Private Build)"
)
@Component
public class CreditResponseMapperImpl implements CreditResponseMapper {

    @Override
    public CreditAnalysisResponse from(CreditAnalysisEntity creditAnalysis) {
        if ( creditAnalysis == null ) {
            return null;
        }

        UUID id = null;
        UUID clientId = null;
        Boolean approved = null;
        BigDecimal requestedAmount = null;
        BigDecimal approvedLimit = null;
        BigDecimal withdraw = null;
        Double annualInterest = null;
        LocalDateTime date = null;

        id = creditAnalysis.getId();
        clientId = creditAnalysis.getClientId();
        approved = creditAnalysis.getApproved();
        requestedAmount = creditAnalysis.getRequestedAmount();
        approvedLimit = creditAnalysis.getApprovedLimit();
        withdraw = creditAnalysis.getWithdraw();
        if ( creditAnalysis.getAnnualInterest() != null ) {
            annualInterest = creditAnalysis.getAnnualInterest().doubleValue();
        }
        date = creditAnalysis.getDate();

        CreditAnalysisResponse creditAnalysisResponse = new CreditAnalysisResponse( id, clientId, approved, requestedAmount, approvedLimit, withdraw, annualInterest, date );

        return creditAnalysisResponse;
    }

    @Override
    public CreditAnalysisResponse mapToResponse(CreditAnalysisEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        UUID clientId = null;
        Boolean approved = null;
        BigDecimal requestedAmount = null;
        BigDecimal approvedLimit = null;
        BigDecimal withdraw = null;
        Double annualInterest = null;
        LocalDateTime date = null;

        id = entity.getId();
        clientId = entity.getClientId();
        approved = entity.getApproved();
        requestedAmount = entity.getRequestedAmount();
        approvedLimit = entity.getApprovedLimit();
        withdraw = entity.getWithdraw();
        if ( entity.getAnnualInterest() != null ) {
            annualInterest = entity.getAnnualInterest().doubleValue();
        }
        date = entity.getDate();

        CreditAnalysisResponse creditAnalysisResponse = new CreditAnalysisResponse( id, clientId, approved, requestedAmount, approvedLimit, withdraw, annualInterest, date );

        return creditAnalysisResponse;
    }
}
