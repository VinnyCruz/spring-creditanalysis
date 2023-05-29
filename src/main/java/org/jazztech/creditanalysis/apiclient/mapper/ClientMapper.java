package org.jazztech.creditanalysis.apiclient.mapper;

import org.jazztech.creditanalysis.controller.response.CreditAnalysisResponse;
import org.jazztech.creditanalysis.repository.entity.CreditAnalysisEntity;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    default List<Client> mapToResponseList(List<CreditAnalysisEntity> entities) {
        return entities.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
