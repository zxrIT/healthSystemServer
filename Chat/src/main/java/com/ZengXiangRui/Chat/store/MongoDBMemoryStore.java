package com.ZengXiangRui.Chat.store;

import com.ZengXiangRui.Chat.entity.ChatMemoryMessagesEntity;
import com.ZengXiangRui.Chat.store.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@SuppressWarnings("all")
@RequiredArgsConstructor
public class MongoDBMemoryStore implements ChatHistoryRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(String userId, String chatId) {
        ChatMemoryMessagesEntity chatMemoryMessagesEntity = new ChatMemoryMessagesEntity();
        chatMemoryMessagesEntity.setUserId(userId);
        chatMemoryMessagesEntity.setChatId(chatId);
        mongoTemplate.insert(chatMemoryMessagesEntity);
    }

    @Override
    public List<String> getChatIds(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        List<ChatMemoryMessagesEntity> chatMemoryMessagesEntities
                = mongoTemplate.find(query, ChatMemoryMessagesEntity.class);
        List<String> chatIds = new ArrayList<>();
        chatMemoryMessagesEntities.stream().forEach(chatMemoryMessagesEntity -> {
            chatIds.add(chatMemoryMessagesEntity.getChatId());
        });
        return chatIds;
    }
}
