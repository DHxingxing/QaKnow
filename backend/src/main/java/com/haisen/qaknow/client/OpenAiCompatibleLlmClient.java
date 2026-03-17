package com.haisen.qaknow.client;

import com.haisen.qaknow.config.LlmProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiCompatibleLlmClient implements LlmClient {

    private final LlmProperties properties;
    private final RestTemplate restTemplate;

    public OpenAiCompatibleLlmClient(LlmProperties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    @Override
    public String complete(String prompt) {
        if (properties.isMock() || properties.getBaseUrl() == null || properties.getApiKey() == null) {
            return "【Mock回答】基于当前检索内容可得：\n" + prompt.substring(0, Math.min(200, prompt.length()));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getApiKey());
        Map<String, Object> request = Map.of(
                "model", properties.getModel(),
                "messages", List.of(Map.of("role", "user", "content", prompt))
        );
        Map<?, ?> response = restTemplate.postForObject(properties.getBaseUrl() + "/chat/completions",
                new HttpEntity<>(request, headers), Map.class);
        try {
            List<?> choices = (List<?>) response.get("choices");
            Map<?, ?> first = (Map<?, ?>) choices.getFirst();
            Map<?, ?> message = (Map<?, ?>) first.get("message");
            return String.valueOf(message.get("content"));
        } catch (Exception e) {
            return "根据当前知识库内容无法确认";
        }
    }
}
