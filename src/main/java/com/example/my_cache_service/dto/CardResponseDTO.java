package com.example.my_cache_service.dto;

public record CardResponseDTO(Long id,
                              String imageLink,
                              String name,
                              Double purchasePrice,
                              Double marketPrice,
                              Double amountChange,
                              Double percentChange) {
}
