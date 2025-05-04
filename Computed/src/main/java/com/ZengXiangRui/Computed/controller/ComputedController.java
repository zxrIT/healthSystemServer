package com.ZengXiangRui.Computed.controller;

import com.ZengXiangRui.Computed.service.ComputedService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

    @GetMapping("/sendMessage")
    public String sendMessage() {
        return computedService.sendMessage();
    }

    @PostMapping("/setUserSalary/{salary}")
    public String setUserSalary(@PathVariable double salary) {
        return computedService.setUserSalary(salary);
    }

    @GetMapping("/getUser")
    public String getUser() {
        return computedService.getUser();
    }

    @PostMapping("/serUserThreshold/{threshold}")
    public String serUserThreshold(@PathVariable int threshold) {
        return computedService.serUserThreshold(threshold);
    }

    @GetMapping("/getUploadTime")
    public String getUploadTime() {
        return computedService.getUploadTime();
    }

    @GetMapping("/getDateDayData/{startTime}/{endTime}")
    public String getDateDayData(@PathVariable Date startTime, @PathVariable Date endTime) {
        return computedService.getDateDayData(startTime, endTime);
    }
}
