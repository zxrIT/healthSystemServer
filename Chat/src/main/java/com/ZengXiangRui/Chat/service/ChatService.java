package com.ZengXiangRui.Chat.service;

import com.ZengXiangRui.Chat.entity.MessageVO;

import java.util.List;

public interface ChatService {
    List<String> getChatIds();

    String chat(String prompt, String chatId);

    List<MessageVO> getChatHistory(String chatId);

    void createChatId(String chatId);
}
