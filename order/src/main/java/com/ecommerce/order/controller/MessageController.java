package com.ecommerce.order.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @GetMapping("/message")
    @RateLimiter(name = "rateBreaker", fallbackMethod = "getMessageFallback")
    public String getMessage() {
        return "Message";
    }

    public String getMessageFallback(Exception exception) {
        return "Fallback Message";
    }
}
