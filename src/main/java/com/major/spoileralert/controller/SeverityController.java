package com.major.spoileralert.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SeverityController {

    @GetMapping("/severities")
    public ResponseEntity<Map<String, String>> getSeverityLabels() {
        Map<String, String> severities = new HashMap<>();
        severities.put("EMERGENCY", "🚨 Emergency");
        severities.put("URGENT", "⚠️ Urgent");
        severities.put("WARNING", "⚡ Warning");
        severities.put("INFO", "ℹ️ Info");
        return ResponseEntity.ok(severities);
    }

}
