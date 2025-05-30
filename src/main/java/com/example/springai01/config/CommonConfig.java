package com.example.springai01.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.example.springai01.constants.SystemConstants;
import com.example.springai01.tools.CourseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.springai01.constants.SystemConstants.CUSTOMER_SERVICE_SYSTEM;


@Configuration
public class CommonConfig {
    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        return chatClientBuilder
                .defaultSystem("你是一个智能客服，可以处理用户的任何问题，叫美羊羊")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .build();
    }

    @Bean
    public ChatClient gameChatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        return chatClientBuilder
                .defaultSystem(SystemConstants.GAME_SYSTEM_PROMPT)
                .defaultAdvisors(new SimpleLoggerAdvisor(), new MessageChatMemoryAdvisor(chatMemory))
                .build();
    }

    @Bean
    public ChatClient serviceChatClient(
            ChatClient.Builder chatClientBuilder,
            ChatMemory chatMemory,
            CourseTools courseTools) {
        return chatClientBuilder
                .defaultSystem(CUSTOMER_SERVICE_SYSTEM)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory), // CHAT MEMORY
                        new SimpleLoggerAdvisor())
                .defaultTools(courseTools)
                .build();
    }

    @Bean
    public ChatClient pdfChatClient(
            ChatClient.Builder chatClientBuilder,
            ChatMemory chatMemory,
            VectorStore vectorStore) {
        return chatClientBuilder
                .defaultSystem("请根据提供的上下文回答问题，不要自己猜测。")
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory), // CHAT MEMORY
                        new SimpleLoggerAdvisor(),
                        new QuestionAnswerAdvisor(
                                vectorStore, // 向量库
                                SearchRequest.builder() // 向量检索的请求参数
                                        .similarityThreshold(0.5d) // 相似度阈值
                                        .topK(2) // 返回的文档片段数量
                                        .build()
                        )
                )
                .build();
    }
}
