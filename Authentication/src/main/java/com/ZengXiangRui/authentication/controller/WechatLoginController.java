package com.ZengXiangRui.authentication.controller;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.ZengXiangRui.authentication.service.WechatService;
import com.ZengXiangRui.authentication.utils.WechatAttributes;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/authentication/wechat")
public class WechatLoginController {
    private final String redirectUrl = URLEncoder.encode(
            "http://24c78abe.r11.cpolar.top/authentication/wechat/login/callback", StandardCharsets.UTF_8);
    private final WechatService wechatService;

    @Autowired
    public WechatLoginController(WechatService wechatService) {
        this.wechatService = wechatService;
    }

    @GetMapping("/verification")
    public String wechatVerification(@RequestParam("signature") String signature,
                                     @RequestParam("timestamp") String timestamp,
                                     @RequestParam("nonce") String nonce,
                                     @RequestParam("echostr") String echostr) {
        return wechatService.wechatVerification(signature, timestamp, nonce, echostr);
    }

    @GetMapping("/login/{loginId}")
    @ResponseBody
    public void wechatLogin(@PathVariable String loginId, HttpServletResponse response) throws IOException {
        String dimensionalUrl = "https://open.weixin.qq.com/connect/oauth2/authorize" +
                "?appid=" + WechatAttributes.appid + "&redirect_uri=" + redirectUrl + "&response_type=code&" +
                "scope=" + WechatAttributes.scope + "&state=" + loginId + "#wechat_redirect";
        response.setContentType("image/png");
        QrConfig qrConfig = new QrConfig(200, 200);
        qrConfig.setErrorCorrection(ErrorCorrectionLevel.H);
        QrCodeUtil.generate(dimensionalUrl, qrConfig, "png", response.getOutputStream());
    }

    @GetMapping("/login/callback")
    public String wechatLoginCallback(String code, String state) {
        return wechatService.wechatLogin(code, state);
    }
}
