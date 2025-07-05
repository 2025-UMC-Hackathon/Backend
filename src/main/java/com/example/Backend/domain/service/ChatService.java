package com.example.Backend.domain.service;

import com.example.Backend.domain.dto.ChatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RestTemplate restTemplate;

    @Value("${perplexity.api.url}")
    private String apiUrl;

    @Value("${perplexity.api.key}")
    private String apiKey;

    public ChatResponseDto askQuestion(String userMessage) {
        // 1. 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 2. 요청 바디 구성
        Map<String, Object> request = new HashMap<>();
        request.put("model", "sonar-pro");
        request.put("stream", false);
        request.put("max_tokens", 1024);
        request.put("frequency_penalty", 1);
        request.put("temperature", 0.0);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "Be precise and concise in your responses."));
        messages.add(Map.of("role", "user", "content", userMessage));
        request.put("messages", messages);

        // 3. HTTP 요청 보내기
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, httpEntity, Map.class);

        // 4. 응답에서 message.content 파싱
        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("choices")) {
            throw new RuntimeException("Invalid response from Perplexity API");
        }

        List<?> choices = (List<?>) responseBody.get("choices");
        if (choices.isEmpty()) {
            throw new RuntimeException("No response from Perplexity");
        }

        Map<String, Object> choice = (Map<String, Object>) choices.get(0);
        Map<String, String> message = (Map<String, String>) choice.get("message");
        String answer = message.get("content");

        return new ChatResponseDto(answer);
    }
}
