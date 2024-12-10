package com.example.backend.FrontendService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "http://localhost:3000")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    // Endpoint to fetch logs
    @GetMapping
    public ResponseEntity<List<String>> getLogs() {
        List<String> logs = logService.getLogs();
        return ResponseEntity.ok(logs);
    }

    // Optional: Endpoint to clear logs
    @PostMapping("/clear")
    public ResponseEntity<String> clearLogs() {
        logService.clearLogs();
        return ResponseEntity.ok("Logs cleared!");
    }
}