package com.report.report_api_pattern.controller;

import com.report.report_api_pattern.client.DeepSeekClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deepseek")
public class DeepSeekController {

    private final DeepSeekClient deepSeekClient;

    public DeepSeekController(DeepSeekClient deepSeekClient) {
        this.deepSeekClient = deepSeekClient;
    }

    @PostMapping("/ask")
    public String askDeepSeek(@RequestBody String question) {
        return deepSeekClient.ask(question);
    }
}
