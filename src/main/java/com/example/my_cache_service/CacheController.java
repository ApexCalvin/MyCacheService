package com.example.my_cache_service;

import com.example.my_cache_service.dto.CardRequestDTO;
import com.example.my_cache_service.dto.CardResponseDTO;
import com.example.my_cache_service.service.CacheService;
import com.example.my_cache_service.service.FetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/myCacheService")
public class CacheController {
    @Autowired
    CacheService cacheService;

    @PostMapping()
    public ResponseEntity<List<CardResponseDTO>> startCaching(@RequestBody List<CardRequestDTO> cards) {
        List<CardResponseDTO> response = cacheService.getCardsWithMetrics(cards);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/disconnectScryfall")
    public ResponseEntity<String> toggleOff() {
        FetchService.scryfallConnection = false;
        return new ResponseEntity<>("Scryfall API disconnected", HttpStatus.OK);
    }

    @PostMapping("/connectScryfall")
    public ResponseEntity<String> toggleOn() {
        FetchService.scryfallConnection = true;
        return new ResponseEntity<>("Scryfall API connected", HttpStatus.OK);
    }
}
