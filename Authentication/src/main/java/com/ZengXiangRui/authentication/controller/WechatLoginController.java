package com.ZengXiangRui.authentication.controller;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.ZengXiangRui.authentication.service.WechatService;
import com.ZengXiangRui.authentication.utils.WechatAttributes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/authentication/wechat")
public class WechatLoginController {

    private static final Logger log = LogManager.getLogger(WechatLoginController.class);
    private final String rediretUrl = URLEncoder.encode(
            "http://6d38f128.r11.cpolar.top/authentication/wechat/login/callback", "UTF-8");
    private final WechatService wechatService;

    @Autowired
    public WechatLoginController(WechatService wechatService) throws UnsupportedEncodingException {
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
                "?appid=" + WechatAttributes.appid + "&redirect_uri=" + rediretUrl + "&response_type=code&" +
                "scope=" + WechatAttributes.scope + "&state=" + loginId + "#wechat_redirect";
        response.setContentType("image/png");
        QrCodeUtil.generate(dimensionalUrl, 200, 200, "png", response.getOutputStream());
    }

    @GetMapping("/login/callback")
    public String wechatLoginCallback(String code, String state) {
        return wechatService.wechatLogin(code, state);
    }
}
