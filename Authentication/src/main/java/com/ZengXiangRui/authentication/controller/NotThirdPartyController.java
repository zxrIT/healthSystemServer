package com.ZengXiangRui.authentication.controller;

import com.ZengXiangRui.authentication.entity.PasswordLoginPayload;
import com.ZengXiangRui.authentication.service.NotThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication/password")
public class NotThirdPartyController {
    private final NotThirdPartyService notThirdPartyService;

    @Autowired
    public NotThirdPartyController(NotThirdPartyService notThirdParty) {
        this.notThirdPartyService = notThirdParty;
    }

    @PostMapping("/login")
    public String passwordLogin(@RequestBody PasswordLoginPayload passwordLoginPayload) {
        return notThirdPartyService.login(passwordLoginPayload.getUsername(), passwordLoginPayload.getPassword());
    }
}
