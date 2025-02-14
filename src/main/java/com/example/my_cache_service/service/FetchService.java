package com.example.my_cache_service.service;

import com.example.my_cache_service.dto.CardRequestDTO;
import com.example.my_cache_service.dto.CardResponseDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FetchService {
    static HttpClient client = HttpClient.newHttpClient();
    public static Boolean scryfallConnection = true;

    public static CardResponseDTO fetchDataFromScryfall(CardRequestDTO card) {
        String scryfallURL = "";

        if(scryfallConnection) {
            scryfallURL = "https://api.scryfall.com/cards/" + card.cardSet() + "/" + card.collectorNumber();
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(scryfallURL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            CardResponseDTO withCalculations = CalculateService.calculateAndMapData(card, response.body());

            return withCalculations;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching card data...", e);
        }
    }
}
