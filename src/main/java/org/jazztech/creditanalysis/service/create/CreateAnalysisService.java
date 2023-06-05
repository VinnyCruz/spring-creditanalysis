package org.jazztech.creditanalysis.service.create;

import feign.FeignException;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jazztech.creditanalysis.apiclient.ClientApi;
import org.jazztech.creditanalysis.apiclient.dto.Client;
import org.jazztech.creditanalysis.controller.request.CreditAnalysisRequest;
import org.jazztech.creditanalysis.controller.response.CreditAnalysisResponse;
import org.jazztech.creditanalysis.exception.ClientIdNotFoundException;
import org.jazztech.creditanalysis.mapper.CreditEntityMapper;
import org.jazztech.creditanalysis.mapper.CreditModelMapper;
import org.jazztech.creditanalysis.mapper.CreditResponseMapper;
import org.jazztech.creditanalysis.model.CreditAnalysis;
import org.jazztech.creditanalysis.repository.CreditAnalysisRepository;
import org.jazztech.creditanalysis.repository.entity.CreditAnalysisEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CreateAnalysisService {
    private final CreditAnalysisRepository creditAnalysisRepository;
    private final CreditResponseMapper creditResponseMapper;
    private final CreditModelMapper creditModelMapper;
    private final CreditEntityMapper creditEntityMapper;
    private final ClientApi clientApi;

    public CreditAnalysis checkIfClientExists(CreditAnalysisRequest creditAnalysisRequest) {
        final CreditAnalysis creditAnalysis = creditModelMapper.from(creditAnalysisRequest);
        final UUID clientIdThatCameFromRequest = creditAnalysis.clientId();
        try {
            final Client clientOfClientApi = returnClientFoundInClientApi(clientIdThatCameFromRequest);
            final CreditAnalysis analysisWithClientIdUpdated = creditAnalysis
                    .returnAnalysisWithClientId(clientOfClientApi);
            return analysisWithClientIdUpdated;
        } catch (FeignException exception) {
            throw new ClientIdNotFoundException("Could not found Client of ClientAPI by Id %s".formatted(clientIdThatCameFromRequest));
        }
    }

    public CreditAnalysis checkIfIsApproved(CreditAnalysisRequest creditAnalysisRequest) {
        final CreditAnalysis pendentAnalysis = checkIfClientExists(creditAnalysisRequest);
        final BigDecimal monthlyIncome = creditAnalysisRequest.monthlyIncome();
        final BigDecimal requestedAmount = creditAnalysisRequest.requestedAmount();
        final boolean approved;
        final BigDecimal approvedlimit;
        final BigDecimal annualInterest = BigDecimal.valueOf(0.15);
        final int limitIncome = 50000;
        final BigDecimal consideredIncome = monthlyIncome.compareTo(BigDecimal.valueOf(limitIncome)) > 0
                ? BigDecimal.valueOf(limitIncome)
                : monthlyIncome;
        // verifica se requestedAmount é maior que a renda
        final CreditAnalysis creditAnalysed;
        if (requestedAmount.compareTo(consideredIncome) > 0) {
            approved = false;
            creditAnalysed = pendentAnalysis
                    .returnAnalysisApprovedFalse(approved);
        } else {
            approved = true;
            // define o valor do limite de crédito se o solicitado for maior que 50% da renda,
            // aprova 15% da renda, se não, aprova 30% da renda.
            final BigDecimal limitPercentageIfRequestIsGreaterThanFifthPercent = BigDecimal.valueOf(0.15);
            final BigDecimal limitPercentageIfRequestIsLessThanFifthPercent = BigDecimal.valueOf(0.30);
            if (requestedAmount.compareTo(consideredIncome.multiply(BigDecimal.valueOf(0.5))) > 0) {
                approvedlimit = consideredIncome.multiply(limitPercentageIfRequestIsGreaterThanFifthPercent);
            } else {
                approvedlimit = consideredIncome.multiply(limitPercentageIfRequestIsLessThanFifthPercent);
            }
            final BigDecimal withdraw = approvedlimit.multiply(BigDecimal.valueOf(0.1));
            creditAnalysed = pendentAnalysis
                    .returnAnalysisApprovedTrue(approved, approvedlimit, withdraw, annualInterest);

        }
        return creditAnalysed;
    }

    public CreditAnalysisResponse makeAnalysisRequest(CreditAnalysisRequest creditAnalysisRequest) {
        final CreditAnalysis analysisWithClientIdUpdated = checkIfIsApproved(creditAnalysisRequest);
        final CreditAnalysisEntity creditAnalysisEntity = creditEntityMapper.from(analysisWithClientIdUpdated);
        final CreditAnalysisEntity analysisSavedInDb = saveAnalysisRequest(creditAnalysisEntity);
        final CreditAnalysisResponse entityToResponse = creditResponseMapper.from(analysisSavedInDb);
        return entityToResponse;
    }

    private CreditAnalysisEntity saveAnalysisRequest(CreditAnalysisEntity creditAnalysisEntity) {
        final CreditAnalysisEntity analysisSavedInDb;
        analysisSavedInDb = creditAnalysisRepository.save(creditAnalysisEntity);
        return analysisSavedInDb;
    }

    public Client returnClientFoundInClientApi(UUID id) {
        final Client client = clientApi.getClientById(id);
        if (clientApi.getClientById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return client;
    }
}
