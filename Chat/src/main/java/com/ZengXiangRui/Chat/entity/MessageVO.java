package com.ZengXiangRui.Chat.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;

@Data
@NoArgsConstructor
public class MessageVO {
    public String type;
    public String content;

    public MessageVO(Message message) {
        switch (message.getMessageType()) {
            case USER -> type = "user";
            case ASSISTANT -> type = "ai";
            default -> type = "unknown";
        }
        this.content = message.getText();
    }
}
