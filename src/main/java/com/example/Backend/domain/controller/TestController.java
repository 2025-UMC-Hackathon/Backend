package com.example.Backend.domain.controller;

import com.example.Backend.global.apiPayload.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestController {

    @GetMapping("test")
    public CustomResponse<String> test() {
        return CustomResponse.ok("test");
    }
}
