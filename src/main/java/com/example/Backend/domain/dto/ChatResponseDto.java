package com.example.Backend.domain.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatResponseDto {
    private String answer;
}

//@Data
//public class PerplexityResponseDto {
//    private String id;
//    private String model;
//    private String object;
//    private long created;
//    private List<Choice> choices;
//
//    @Data
//    public static class Choice {
//        private int index;
//        private String finish_reason;
//        private Message message;
//
//        @Data
//        public static class Message {
//            private String role;
//            private String content;
//        }
//    }
//}
