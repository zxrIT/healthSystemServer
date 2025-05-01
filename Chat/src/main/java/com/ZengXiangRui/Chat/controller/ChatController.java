package com.ZengXiangRui.Chat.controller;

import com.ZengXiangRui.Chat.entity.MessageVO;
import com.ZengXiangRui.Chat.service.ChatService;
import com.ZengXiangRui.Common.Response.BaseResponse;
import com.ZengXiangRui.Common.Response.BaseResponseUtil;
import com.ZengXiangRui.Common.Utils.JsonSerialization;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SuppressWarnings("all")
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ChatController {

    @Autowired
    private final ChatService chatService;


    @GetMapping("/chat/getChatIds")
    public String getChatIds() {
        List<String> chatIds = chatService.getChatIds();
        return JsonSerialization.toJson(new BaseResponse<List<String>>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, chatIds
        ));
    }

    @GetMapping("/chat/{prompt}/{chatId}")
    public String Chat(@PathVariable String prompt, @PathVariable String chatId) {
        String chatMessage = chatService.chat(prompt, chatId);
        return JsonSerialization.toJson(new BaseResponse<String>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, chatMessage
        ));
    }

    @GetMapping("/chat/{chatId}")
    public String getChatHistory(@PathVariable String chatId) {
        List<MessageVO> chatHistory = chatService.getChatHistory(chatId);
        return JsonSerialization.toJson(new BaseResponse<List<MessageVO>>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, chatHistory
        ));
    }

    @PostMapping("/chat/create/{chatId}")
    public String createChat(@PathVariable String chatId) {
        chatService.createChatId(chatId);
        return JsonSerialization.toJson(new BaseResponse<String>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "success"
        ));
    }
}
