package com.ZengXiangRui.Computed.controller;

import com.ZengXiangRui.Computed.service.ComputedService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings("all")
@RequestMapping("/computed")
@RequiredArgsConstructor
public class ComputedController {

    @Autowired
    private final ComputedService computedService;

    @GetMapping("/getSurplus")
    public String getSurplus() {
        return computedService.getTotalSpending();
    }

    @GetMapping("/getUploadTime")
    public String getUploadTime() {
        return computedService.getUploadTime();
    }
}
