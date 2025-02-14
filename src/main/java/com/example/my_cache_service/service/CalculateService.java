package com.example.my_cache_service.service;

import com.example.my_cache_service.dto.CardRequestDTO;
import com.example.my_cache_service.dto.CardResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CalculateService {
    public static CardResponseDTO calculateAndMapData(CardRequestDTO card, String scryfall_JSON) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode scryfallNode = objectMapper.readTree(scryfall_JSON);

        String imageLink = scryfallNode.path("image_uris").path("normal").asText();
        String name = scryfallNode.path("name").asText();

        double marketPrice = 0.00;
        Boolean isFoil = Boolean.parseBoolean(scryfallNode.path("foil").asText());
        if(isFoil) {
            marketPrice = Double.parseDouble(scryfallNode.path("usd_foil").asText());
        } else {
            marketPrice = Double.parseDouble(scryfallNode.path("usd").asText());
        }

        double amountChange = (marketPrice - card.purchasePrice());
        amountChange = Double.parseDouble(String.format("%.2f", amountChange));

        double percentChange = ((100 * (marketPrice / card.purchasePrice())) - 100);
        if(Double.isFinite(percentChange)) {
            percentChange = 0.00;
        } else {
            percentChange = Double.parseDouble(String.format("%.2f", percentChange));
        }

        return new CardResponseDTO(card.id(),
                imageLink,
                name,
                card.purchasePrice(),
                marketPrice,
                amountChange,
                percentChange);
    }

}
