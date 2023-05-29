package org.jazztech.creditanalysis.service.create;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.jazztech.creditanalysis.exception.ClientIdNotFoundException;
import org.springframework.stereotype.Service;
import org.jazztech.creditanalysis.apiclient.ClientApi;
import org.jazztech.creditanalysis.apiclient.dto.Client;
import org.jazztech.creditanalysis.controller.request.CreditAnalysisRequest;
import org.jazztech.creditanalysis.controller.response.CreditAnalysisResponse;
import org.jazztech.creditanalysis.mapper.CreditEntityMapper;
import org.jazztech.creditanalysis.mapper.CreditModelMapper;
import org.jazztech.creditanalysis.mapper.CreditResponseMapper;
import org.jazztech.creditanalysis.model.CreditAnalysis;
import org.jazztech.creditanalysis.repository.CreditAnalysisRepository;
import org.jazztech.creditanalysis.repository.entity.CreditAnalysisEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class    CreateAnalysisService {
    private final CreditAnalysisRepository creditAnalysisRepository;
    private final CreditResponseMapper creditResponseMapper;
    private final CreditModelMapper creditModelMapper;
    private final CreditEntityMapper creditEntityMapper;
    private final ClientApi clientApi;

    public CreditAnalysis checkIfClientExists (CreditAnalysisRequest creditAnalysisRequest) {
        final CreditAnalysis creditAnalysis = creditModelMapper.from(creditAnalysisRequest);
        final UUID clientIdThatCameFromRequest = creditAnalysis.clientId();
        try {
            final Client clientOfClientApi = returnClientFoundInClientApi(clientIdThatCameFromRequest);
            final CreditAnalysis analysisWithClientIdUpdated = creditAnalysis
                    .returnAnalysisWithClientId(clientOfClientApi);
            return analysisWithClientIdUpdated;
        } catch (FeignException exception) {
            throw new ClientIdNotFoundException
                    ("Could not found Client of ClientAPI by Id %s".formatted(clientIdThatCameFromRequest));
        }
    }

    public CreditAnalysis checkIfIsApproved (CreditAnalysisRequest creditAnalysisRequest) {
        final CreditAnalysis pendentAnalysis = checkIfClientExists(creditAnalysisRequest);
        final BigDecimal monthlyIncome = creditAnalysisRequest.monthlyIncome();
        final BigDecimal requestedAmount = creditAnalysisRequest.requestedAmount();
        final boolean approved;
        final BigDecimal approvedlimit;
        final BigDecimal withdraw;
        final BigDecimal annualInterest;
        // Op. Ternário compara renda mensal com 50k e se resultar em maior que a renda mensal,
        // consideredIncome será 50k, caso não, será a própria monthlyIncome.
        BigDecimal consideredIncome = monthlyIncome.compareTo(BigDecimal.valueOf(50000)) > 0
                ? BigDecimal.valueOf(50000)
                : monthlyIncome;
        // verifica se requestedAmount é maior que a renda
        CreditAnalysis creditAnalysed;
        if (requestedAmount.compareTo(consideredIncome) > 0) {
            approved = false;
            creditAnalysed = pendentAnalysis
                    .returnAnalysisApprovedFalse(approved);
        } else {
            // define o valor do limite de crédito se o solicitado for maior que 50% da renda,
            // aprova 15% da renda, se não, aprova 30% da renda.
            final BigDecimal limitPercentage;
            if (requestedAmount.compareTo(consideredIncome.multiply(BigDecimal.valueOf(0.5))) > 0) {
                limitPercentage = BigDecimal.valueOf(0.15);
            } else {
                limitPercentage = BigDecimal.valueOf(0.3);
            }
            // Por fim, valor de crédito aprovado.
            approvedlimit = consideredIncome.multiply(limitPercentage);
            approved = true;
            withdraw = approvedlimit.multiply(BigDecimal.valueOf(0.1));
            annualInterest = BigDecimal.valueOf(0.15);

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
