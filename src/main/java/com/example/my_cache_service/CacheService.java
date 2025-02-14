package com.example.my_cache_service;

import com.example.my_cache_service.dto.CardRequestDTO;
import com.example.my_cache_service.dto.CardResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CacheService {
    CacheStore<Long, CardResponseDTO> cacheStore = new CacheStore<Long, CardResponseDTO>();
    HttpClient client = HttpClient.newHttpClient();

    public List<CardResponseDTO> run(List<CardRequestDTO> incomingCards) {
        List<CardResponseDTO> outgoingCards = new ArrayList<>();

        if(cacheStore.isEmpty()) {
            System.out.println("Cache is empty. Fetch data...");

            for (CardRequestDTO card: incomingCards) {
                CardResponseDTO newCard = fetchDataFromScryfall(card);
                cacheStore.save(card.id(), newCard);
                outgoingCards.add(newCard);
            }
            return outgoingCards;
        } else {
            System.out.println("Searching from cache...");

            for (CardRequestDTO card: incomingCards) {
                if(cacheStore.containsKey(card.id())) {
                    CardResponseDTO existingCard = cacheStore.get(card.id());
                    outgoingCards.add(existingCard);
                } else {
                    CardResponseDTO newCard = fetchDataFromScryfall(card);
                    cacheStore.save(card.id(), newCard);
                    outgoingCards.add(newCard);
                }
            }
            return outgoingCards;
        }
    }

    public CardResponseDTO fetchDataFromScryfall(CardRequestDTO card) {
        String scryfallURL = "https://api.scryfall.com/cards/" + card.cardSet() + "/" + card.collectorNumber();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(scryfallURL))
                        .GET()
                        .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            CardResponseDTO withCalculations = calculateAndMapData(card, response.body());

            return withCalculations;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching card data...", e);
        }
    }

    public CardResponseDTO calculateAndMapData(CardRequestDTO card, String scryfall_JSON) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode scryfallNode = objectMapper.readTree(scryfall_JSON);

        Boolean isFoil = Boolean.parseBoolean(scryfallNode.path("foil").asText());

        double marketPrice = 0.00;
        if(isFoil) {
            marketPrice = Double.parseDouble(scryfallNode.path("usd_foil").asText());
        } else {
            marketPrice = Double.parseDouble(scryfallNode.path("usd").asText());
        }

        double percentChange = ((100 * (marketPrice / card.purchasePrice())) - 100);
        if(Double.isFinite(percentChange)) {
            percentChange = 0.00;
        } else {
            percentChange = Double.parseDouble(String.format("%.2f", percentChange));
        }

        double amountChange = (marketPrice - card.purchasePrice());
        amountChange = Double.parseDouble(String.format("%.2f", amountChange));

        String imageLink = scryfallNode.path("image_uris.normal").asText();

        return new CardResponseDTO(card.id(),
                                    imageLink,
                                    scryfallNode.path("name").asText(),
                                    card.purchasePrice(),
                                    marketPrice,
                                    amountChange,
                                    percentChange);
    }

    public static void refreshCache() {
        //get from cache
        //iterate through
        //fetch from Scryfall
        //calculate & update DTOs
        //save to cache
    }

    public static void dailyUpdateChecker(int refreshHour) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if(now.getHour() == refreshHour) {
            refreshCache();
            System.out.println("Cache has been refreshed @ " + now.format(formatter));
        }
    }
}
