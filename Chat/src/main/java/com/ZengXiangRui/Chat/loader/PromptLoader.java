package com.ZengXiangRui.Chat.loader;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@SuppressWarnings("all")
@RequiredArgsConstructor
public class PromptLoader {

    @Autowired
    private final ResourceLoader resourceLoader;

    public String loadSystemPrompt() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:chat-prompt-template.txt");
        try (var reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            String promptTemplate = FileCopyUtils.copyToString(reader);
            return promptTemplate.replace("{{current_date}}",
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
        }
    }
}
