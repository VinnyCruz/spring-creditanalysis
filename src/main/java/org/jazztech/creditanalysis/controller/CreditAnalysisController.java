package org.jazztech.creditanalysis.controller;

import lombok.RequiredArgsConstructor;
import org.jazztech.creditanalysis.controller.request.CreditAnalysisRequest;
import org.jazztech.creditanalysis.controller.response.CreditAnalysisResponse;
import org.jazztech.creditanalysis.repository.CreditAnalysisRepository;
import org.jazztech.creditanalysis.service.create.CreateAnalysisService;
import org.jazztech.creditanalysis.service.search.SearchAnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "v1.0/creditanalysis")
@RequiredArgsConstructor
public class CreditAnalysisController {
    private final CreateAnalysisService createAnalysisService;
    private final SearchAnalysisService searchAnalysisService;
    private final CreditAnalysisRepository creditAnalysisRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreditAnalysisResponse requestAnalysis(@RequestBody CreditAnalysisRequest creditAnalysisRequest) {
        return createAnalysisService.makeAnalysisRequest(creditAnalysisRequest);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public CreditAnalysisResponse getAnalysis(@PathVariable(value = "id") UUID id) {
        return searchAnalysisService.getAnalysisById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public List<CreditAnalysisResponse> getAnalysisByClientId(@RequestParam(value = "id") UUID clientId) {
        return searchAnalysisService.getAllAnalsysisByClientId(clientId);
    }

    @GetMapping(path = "/cpf")
    @ResponseStatus(HttpStatus.FOUND)
    public List<CreditAnalysisResponse> getAnalysisByClientCpf(@RequestParam(value = "cpf") String clientCpf) {
        return searchAnalysisService.getAllAnalsysisByClientCpf(clientCpf);
    }
}
