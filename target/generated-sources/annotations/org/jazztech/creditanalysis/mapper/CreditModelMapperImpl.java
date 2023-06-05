package org.jazztech.creditanalysis.mapper;

import javax.annotation.processing.Generated;
import org.jazztech.creditanalysis.controller.request.CreditAnalysisRequest;
import org.jazztech.creditanalysis.model.CreditAnalysis;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-29T14:04:10-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
@Component
public class CreditModelMapperImpl implements CreditModelMapper {

    @Override
    public CreditAnalysis from(CreditAnalysisRequest creditAnalysisRequest) {
        if ( creditAnalysisRequest == null ) {
            return null;
        }

        CreditAnalysis.CreditAnalysisBuilder creditAnalysis = CreditAnalysis.builder();

        creditAnalysis.clientId( creditAnalysisRequest.clientId() );
        creditAnalysis.requestedAmount( creditAnalysisRequest.requestedAmount() );

        return creditAnalysis.build();
    }
}
