package com.ZengXiangRui.authentication.controller;

import com.ZengXiangRui.authentication.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class LogoutController {
    private final LogoutService logoutService;

    @Autowired
    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @PostMapping("/logout/{userId}")
    private String logout(@PathVariable String userId) {
        return logoutService.logout(userId);
    }

    @PostMapping("/verification/{userId}/{token}")
    private String verification(@PathVariable String userId, @PathVariable String token) {
        return logoutService.verificationToken(userId, token);
    }
}
