package com.example.my_cache_service.dto;

public record CardRequestDTO(Long id,
                            String cardSet,
                            String collectorNumber,
                            Boolean foil,
                            Double purchasePrice) {
}
