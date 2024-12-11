package com.example.backend.FrontendService;

import com.example.backend.Database.LogEntry;
import com.example.backend.Database.LogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class LogService {
    private final List<String> logs = Collections.synchronizedList(new LinkedList<>());

    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;

        // Load existing logs from the database into the in-memory list at startup
        List<LogEntry> existingLogs = logRepository.findAll();
        for (LogEntry logEntry : existingLogs) {
            logs.add(logEntry.getMessage());
        }
    }

    public void addLog(String log) {


        logs.add(log);
        // Limit the size of logs to avoid memory overflow
        if (logs.size() > 1000) {
            logs.remove(0);
        }

        // Save log to the database
        LogEntry logEntry = new LogEntry();
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setLogLevel("INFO"); // Set the log level
        logEntry.setMessage(log);
        logRepository.save(logEntry);
    }

    public List<String> getLogs() {
        return new LinkedList<>(logs);
    }

    public List<LogEntry> getLogsFromDatabase() {
        return logRepository.findAll(); // Retrieve all logs from the database
    }

    public void clearLogs() {
        logs.clear();
    }



}