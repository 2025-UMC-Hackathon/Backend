package com.example.Backend.domain.dto.request;

import lombok.Data;

@Data
public class ChatRequestDto {
    private String message;
}

//@Data
//public class PerplexityRequestDto {
//    private String model;
//    private boolean stream;
//    private int max_tokens;
//    private int frequency_penalty;
//    private double temperature;
//    private List<Message> messages;
//
//    @Data
//    public static class Message {
//        private String role;
//        private String content;
//    }
//}
