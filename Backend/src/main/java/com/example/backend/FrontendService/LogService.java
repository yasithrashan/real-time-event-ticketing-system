package com.example.backend.FrontendService;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class LogService {
    private final List<String> logs = Collections.synchronizedList(new LinkedList<>());

    public void addLog(String log) {


        logs.add(log);
        // Limit the size of logs to avoid memory overflow
        if (logs.size() > 1000) {
            logs.remove(0);
        }

    }

    public List<String> getLogs() {
        return new LinkedList<>(logs);
    }


    public void clearLogs() {
        logs.clear();
    }



}