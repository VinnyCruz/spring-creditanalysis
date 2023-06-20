package org.jazztech.creditanalysis.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreditAnalysisResponse(
    UUID id,
    UUID clientId,
    Boolean approved,
    BigDecimal requestedAmount,
    BigDecimal approvedLimit,
    BigDecimal withdraw,
    Double annualInterest,
    LocalDateTime date
) {
    @Builder(toBuilder = true)
    public CreditAnalysisResponse(UUID id, UUID clientId, Boolean approved, BigDecimal requestedAmount, BigDecimal approvedLimit, BigDecimal withdraw,
                                  Double annualInterest, LocalDateTime date) {
        this.id = id;
        this.clientId = clientId;
        this.approved = approved;
        this.requestedAmount = requestedAmount;
        this.approvedLimit = approvedLimit;
        this.withdraw = withdraw;
        this.annualInterest = annualInterest;
        this.date = date;
    }
}
