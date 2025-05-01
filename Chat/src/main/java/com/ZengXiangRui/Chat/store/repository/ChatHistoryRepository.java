package com.ZengXiangRui.Chat.store.repository;

import java.util.List;

public interface ChatHistoryRepository {
    void save(String userId, String chatId);

    List<String> getChatIds(String userId);
}
