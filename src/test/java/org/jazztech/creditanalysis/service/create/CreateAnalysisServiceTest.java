package org.jazztech.creditanalysis.service.create;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.RoundingMode;
import org.jazztech.creditanalysis.mapper.CreditEntityMapperImpl;
import org.jazztech.creditanalysis.mapper.CreditModelMapperImpl;
import org.jazztech.creditanalysis.mapper.CreditResponseMapperImpl;
import org.jazztech.creditanalysis.model.CreditAnalysis;
import org.junit.jupiter.api.Assertions;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.UUID;
import org.jazztech.creditanalysis.apiclient.ClientApi;
import org.jazztech.creditanalysis.apiclient.dto.Client;
import org.jazztech.creditanalysis.controller.request.CreditAnalysisRequest;
import org.jazztech.creditanalysis.mapper.CreditEntityMapper;
import org.jazztech.creditanalysis.mapper.CreditModelMapper;
import org.jazztech.creditanalysis.mapper.CreditResponseMapper;
import org.jazztech.creditanalysis.repository.CreditAnalysisRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateAnalysisServiceTest {
    @InjectMocks
    private CreateAnalysisService service;

    @Mock
    CreditAnalysisRepository creditAnalysisRepository;

    @Mock
    private ClientApi clientApi;

    @Spy
    private CreditResponseMapper creditResponseMapper = new CreditResponseMapperImpl();

    @Spy
    private CreditModelMapper creditModelMapper = new CreditModelMapperImpl();

    @Spy
    private CreditEntityMapper creditEntityMapper = new CreditEntityMapperImpl();

    @Captor
    private ArgumentCaptor<UUID> clientIdCaptor;
    @Captor
    private ArgumentCaptor<String> clientCpfCaptor;
    @Captor
    private ArgumentCaptor<UUID> analysisId;

    @Test
    void should_disapprove_credit_analysis_request() {
        final Client client = new Client(UUID.randomUUID());
        when(clientApi.getClientById(clientIdCaptor.capture())).thenReturn(client);
        CreditAnalysisRequest request = CreditAnalysisRequest.builder()
                .clientId(client.id())
                .monthlyIncome(new BigDecimal(20000))
                .requestedAmount(new BigDecimal(20001))
                .build();
        CreditAnalysis disapprovedAnalysis = service.approveOrDisapproveAnalysis(request);
        assertEquals(false, disapprovedAnalysis.approved());
    }

    @Test
    void should_approve_credit_analysis_if_request_is_greater_than_fifth_percent() {
        final Client client = new Client(UUID.randomUUID());
        when(clientApi.getClientById(clientIdCaptor.capture())).thenReturn(client);
        CreditAnalysisRequest request = CreditAnalysisRequest.builder()
                .clientId(client.id())
                .monthlyIncome(new BigDecimal(20000))
                .requestedAmount(new BigDecimal(10001))
                .build();
        CreditAnalysis approvedAnalysis = service.approveOrDisapproveAnalysis(request);
        assertEquals(true, approvedAnalysis.approved());
        assertEquals(new BigDecimal(3000).setScale(2, RoundingMode.DOWN), approvedAnalysis.approvedLimit().setScale(2, RoundingMode.DOWN));
    }
}