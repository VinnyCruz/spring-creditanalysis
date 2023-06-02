package org.jazztech.creditanalysis.mapper;

import javax.annotation.processing.Generated;
import org.jazztech.creditanalysis.model.CreditAnalysis;
import org.jazztech.creditanalysis.repository.entity.CreditAnalysisEntity;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-02T04:03:20-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Private Build)"
)
@Component
public class CreditEntityMapperImpl implements CreditEntityMapper {

    @Override
    public CreditAnalysisEntity from(CreditAnalysis creditAnalysis) {
        if ( creditAnalysis == null ) {
            return null;
        }

        CreditAnalysisEntity.CreditAnalysisEntityBuilder creditAnalysisEntity = CreditAnalysisEntity.builder();

        creditAnalysisEntity.clientId( creditAnalysis.clientId() );
        creditAnalysisEntity.approved( creditAnalysis.approved() );
        creditAnalysisEntity.approvedLimit( creditAnalysis.approvedLimit() );
        creditAnalysisEntity.withdraw( creditAnalysis.withdraw() );
        creditAnalysisEntity.annualInterest( creditAnalysis.annualInterest() );
        creditAnalysisEntity.requestedAmount( creditAnalysis.requestedAmount() );
        creditAnalysisEntity.date( creditAnalysis.date() );

        return creditAnalysisEntity.build();
    }
}
