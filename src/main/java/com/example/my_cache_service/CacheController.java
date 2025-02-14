package com.example.my_cache_service;

import com.example.my_cache_service.dto.CardRequestDTO;
import com.example.my_cache_service.dto.CardResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/myCacheService")
public class CacheController {
    @Autowired
    CacheService cacheService;

    @PostMapping()
    public ResponseEntity<List<CardResponseDTO>> startCaching(@RequestBody List<CardRequestDTO> cards) {
        List<CardResponseDTO> response = cacheService.run(cards);
        return ResponseEntity.ok(response);
    }
}
