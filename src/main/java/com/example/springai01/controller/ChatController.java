package com.example.springai01.controller;

import com.example.springai01.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("ai")
@RequiredArgsConstructor
public class ChatController {

    private  final ChatClient chatClient;
    private final ChatHistoryRepository chatHistoryRepository;
    @RequestMapping(value = "chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(String prompt,String chatId) {

        chatHistoryRepository.save("chat",chatId);

        return chatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content();
    }
}
