package com.example.Backend.domain.controller;

import com.example.Backend.domain.dto.ChatRequestDto;
import com.example.Backend.domain.dto.ChatResponseDto;
import com.example.Backend.domain.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatResponseDto> chat(@RequestBody ChatRequestDto requestDto) {
        ChatResponseDto response = chatService.askQuestion(requestDto.getMessage());
        return ResponseEntity.ok(response);
    }

}

