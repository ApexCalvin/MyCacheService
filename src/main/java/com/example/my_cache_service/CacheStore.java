package com.example.my_cache_service;

import java.util.concurrent.ConcurrentHashMap;

public class CacheStore<Long, CardResponseDTO> { //encapsulation
    private ConcurrentHashMap<Long, CardResponseDTO> cache = new ConcurrentHashMap<Long, CardResponseDTO>();

    public CardResponseDTO get(Long id) {
        return cache.get(id);
    }

    public void save(Long id, CardResponseDTO cardResponseDTO) {
        cache.put(id, cardResponseDTO);
    }

    public boolean containsKey(Long id) {
        return cache.containsKey(id);
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }
}
