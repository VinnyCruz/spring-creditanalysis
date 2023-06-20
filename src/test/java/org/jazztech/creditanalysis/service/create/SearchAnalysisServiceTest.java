package org.jazztech.creditanalysis.service.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jazztech.creditanalysis.apiclient.ClientApi;
import org.jazztech.creditanalysis.apiclient.dto.Client;
import org.jazztech.creditanalysis.controller.response.CreditAnalysisResponse;
import org.jazztech.creditanalysis.exception.ClientNotFoundException;
import org.jazztech.creditanalysis.mapper.CreditResponseMapper;
import org.jazztech.creditanalysis.mapper.CreditResponseMapperImpl;
import org.jazztech.creditanalysis.repository.CreditAnalysisRepository;
import org.jazztech.creditanalysis.repository.entity.CreditAnalysisEntity;
import org.jazztech.creditanalysis.service.search.SearchAnalysisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
public class SearchAnalysisServiceTest {
    @Mock
    CreditAnalysisRepository creditAnalysisRepository;
    @InjectMocks
    private SearchAnalysisService service;
    @Mock
    private ClientApi clientApi;

    @Spy
    private CreditResponseMapper creditResponseMapper = new CreditResponseMapperImpl();

    @Captor
    private ArgumentCaptor<String> clientCpfCaptor;


    @Test
    void should_return_an_analysis_searched_by_id() {
        final UUID uuid = UUID.randomUUID();
        when(creditAnalysisRepository.findById(uuid)).thenReturn(Optional.ofNullable(entityFactory()));

        CreditAnalysisResponse response = service.getAnalysisById(uuid);
        assertNotNull(response);
    }

    @Test
    void should_return_a_list_of_analysis() {
        when(creditAnalysisRepository.findAll()).thenReturn(List.of(entityFactory(), entityFactory(), entityFactory(), entityFactory()));
        List<CreditAnalysisResponse> responses = service.getAllCreditAnalyses();
        assertEquals(4, responses.size());
    }

    @Test
    void should_return_list_of_credit_analysis_of_one_client_searched_by_id() {
        final Client client = new Client(UUID.randomUUID());
        when(creditAnalysisRepository.findByClientId(client.id())).thenReturn(
                List.of(entityFactory(), entityFactory(), entityFactory(), entityFactory()));
        List<CreditAnalysisResponse> responses = service.getAllAnalsysisByClientId(client.id());
        assertEquals(4, responses.size());
    }

    @Test
    void should_return_list_of_credit_analysis_of_one_client_searched_by_cpf() {
        final Client client = new Client(UUID.randomUUID());
        when(clientApi.getClientByCpf(clientCpfCaptor.capture())).thenReturn(List.of(client, client, client, client));
        when(creditAnalysisRepository.findByClientId(client.id())).thenReturn(
                List.of(entityFactory(), entityFactory(), entityFactory(), entityFactory()));
        List<CreditAnalysisResponse> responses = service.getAllAnalsysisByClientCpf("45959809818");
        assertEquals(4, responses.size());
    }

    @Test
    void should_throw_response_status_exception_not_found() {
        final UUID uuid = UUID.randomUUID();
        when(creditAnalysisRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.getAnalysisById(uuid));
    }

    @Test
    void should_throw_CliendNotFoundException_when_cpf_not_found_while_searching() {
        when(clientApi.getClientByCpf(clientCpfCaptor.capture())).thenReturn(List.of());
        assertThrows(ClientNotFoundException.class, () -> service.getAllAnalsysisByClientCpf("45959809818"));
    }

    public CreditAnalysisEntity entityFactory() {
        return CreditAnalysisEntity.builder()
                .clientId(UUID.randomUUID())
                .approved(true)
                .approvedLimit(BigDecimal.valueOf(6000))
                .withdraw(BigDecimal.valueOf(600))
                .annualInterest(BigDecimal.valueOf(0.15))
                .requestedAmount(BigDecimal.TEN)
                .build();
    }
}
