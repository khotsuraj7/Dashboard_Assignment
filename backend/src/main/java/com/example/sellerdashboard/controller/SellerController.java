
package com.example.sellerdashboard.controller;

import com.example.sellerdashboard.dto.SellerSummaryDto;
import com.example.sellerdashboard.entity.Seller;
import com.example.sellerdashboard.repository.SellerRepository;
import com.example.sellerdashboard.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/seller")
@CrossOrigin(origins = "*")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private SellerRepository sellerRepository;

    @GetMapping("/{id}/summary")
    public ResponseEntity<?> getSummary(@PathVariable Long id) {
        try {
            SellerSummaryDto summary = sellerService.getSellerSummary(id);
            return ResponseEntity.ok(summary);
        } catch (RuntimeException ex) {
            if ("NOT_FOUND".equals(ex.getMessage())) {
                return ResponseEntity.status(404).body(new ErrorResponse(404, "Seller not found"));
            }
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal server error"));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> listSellers() {
        Iterable<Seller> all = sellerRepository.findAll();
        List<?> list = StreamSupport.stream(all.spliterator(), false)
                .map(s -> new SimpleSeller(s.getId(), s.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    static class ErrorResponse {
        public int status;
        public String message;
        public ErrorResponse(int s, String m) { this.status = s; this.message = m; }
    }

    static class SimpleSeller {
        public Long id;
        public String name;
        public SimpleSeller(Long id, String name) { this.id = id; this.name = name; }
    }
}
