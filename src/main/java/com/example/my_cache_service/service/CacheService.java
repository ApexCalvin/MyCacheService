package com.example.my_cache_service.service;

import com.example.my_cache_service.CacheStore;
import com.example.my_cache_service.dto.CardRequestDTO;
import com.example.my_cache_service.dto.CardResponseDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CacheService {
    CacheStore<Long, CardResponseDTO> cacheStore = new CacheStore<Long, CardResponseDTO>();

    public List<CardResponseDTO> getCardsWithMetrics(List<CardRequestDTO> incomingCards) {
        List<CardResponseDTO> outgoingCards = new ArrayList<>();

        if(cacheStore.isEmpty()) {
            System.out.println("Cache is empty. Fetch data...");

            for (CardRequestDTO card: incomingCards) {
                CardResponseDTO newCard = FetchService.fetchDataFromScryfall(card);
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
                    CardResponseDTO newCard = FetchService.fetchDataFromScryfall(card);
                    cacheStore.save(card.id(), newCard);
                    outgoingCards.add(newCard);
                }
            }
            return outgoingCards;
        }
    }

    public static void refreshCache() {
        //get from cache
        //iterate through
        //fetch from Scryfall
        //calculate & update DTOs
        //save to cache
    }
}
