package com.ZengXiangRui.Chat.service.impl;

import com.ZengXiangRui.Chat.entity.MessageVO;
import com.ZengXiangRui.Chat.service.ChatService;
import com.ZengXiangRui.Chat.store.repository.ChatHistoryRepository;
import com.ZengXiangRui.Common.Utils.UserContext;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@Service
@SuppressWarnings("all")
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    @Autowired
    private final ChatHistoryRepository chatHistoryRepository;

    @Autowired
    private final ChatClient chatClient;

    @Autowired
    private final ChatMemory chatMemory;

    @Override
    @LoggerAnnotation(operation = "查询当前用户的所有会话id", dataSource = "mongodb")
    public List<String> getChatIds() {
        List<String> chatIds =
                chatHistoryRepository.getChatIds(UserContext.getUserId());
        return chatIds;
    }

    @Override
    public String chat(String prompt, String chatId) {
        String chatMessage = chatClient.prompt().user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .call().content();
        return chatMessage;
    }

    @Override
    public List<MessageVO> getChatHistory(String chatId) {
        List<Message> messages = chatMemory.get(chatId, Integer.MAX_VALUE);
        if (messages == null) {
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();
    }

    @Override
    public void createChatId(String chatId) {
        System.out.println(UserContext.getUserId());
        chatHistoryRepository.save(UserContext.getUserId(), chatId);
    }
}
