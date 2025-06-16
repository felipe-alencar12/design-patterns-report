package com.report.report_api_pattern.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.report.report_api_pattern.client.DeepSeekClient;
import com.report.report_api_pattern.repository.UserRepositoryJdbc;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final DeepSeekClient deepSeekClient;
    private final UserRepositoryJdbc userRepository;
    private final ObjectMapper objectMapper;

    public UserService(DeepSeekClient deepSeekClient, UserRepositoryJdbc userRepository) {
        this.deepSeekClient = deepSeekClient;
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
    }

    public void populateFakeUsers(int quantity) {
        try {
            // 🔥 Pergunta para a IA gerar dados no formato JSON
            String prompt = String.format(
                    "Answer in jSON format without formatting, just plain text. Generate %d fake users in JSON array format. Each user should have 'name' and 'email'.",
                    quantity
            );

            String response = deepSeekClient.ask(prompt);

            // 🛠️ Parsear a resposta
            JsonNode jsonNode = objectMapper.readTree(response);

            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    String name = node.get("name").asText();
                    String email = node.get("email").asText();
                    userRepository.save(name, email);
                    System.out.println("Saved user: " + name + " - " + email);
                }
            } else {
                System.out.println("Response is not a JSON array");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
