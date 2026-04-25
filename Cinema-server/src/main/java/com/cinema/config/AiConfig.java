package com.cinema.config;

import com.cinema.entity.Cinema;
import com.cinema.entity.Movie;
import com.cinema.service.CinemaService;
import com.cinema.service.ConsultantService;
import com.cinema.service.MovieService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class AiConfig {

    @Bean
    public StreamingChatLanguageModel streamingChatLanguageModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey(System.getenv("API_KEY"))
                .modelName("qwen2.5-7b-instruct")
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    @Lazy
    public ConsultantService consultantStreamingService(
            StreamingChatLanguageModel streamingModel,
            ChatMemoryProvider chatMemoryProvider,
            ContentRetriever contentRetriever) {
        return AiServices.builder(ConsultantService.class)
                .streamingChatLanguageModel(streamingModel)
                .chatMemoryProvider(chatMemoryProvider)
                .contentRetriever(contentRetriever)
                .build();
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey(System.getenv("API_KEY"))
                .modelName("text-embedding-v1")
                .build();
    }

    @Bean
    @Lazy
    public EmbeddingStore embeddingStore(
            EmbeddingModel embeddingModel,
            MovieService movieService,
            CinemaService cinemaService) {
        
        log.info("开始构建向量存�?..");
        
        List<Document> documents = new ArrayList<>();

        List<Movie> movies = movieService.list(null);
        if (movies != null && !movies.isEmpty()) {
            List<Document> movieDocs = movies.stream()
                    .filter(movie -> movie.getStatus() != null && movie.getStatus() == 1)
                    .map(movie -> {
                        StringBuilder sb = new StringBuilder();
                        sb.append("类型：电影\n");
                        sb.append("电影ID�?).append(movie.getId()).append("\n");
                        sb.append("电影名称�?).append(movie.getName()).append("\n");
                        if (movie.getCategoryId() != null) {
                            sb.append("分类ID�?).append(movie.getCategoryId()).append("\n");
                        }
                        if (movie.getDirector() != null) {
                            sb.append("导演�?).append(movie.getDirector()).append("\n");
                        }
                        if (movie.getActors() != null) {
                            sb.append("主演�?).append(movie.getActors()).append("\n");
                        }
                        if (movie.getReleaseDate() != null) {
                            sb.append("上映日期�?).append(movie.getReleaseDate()).append("\n");
                            if (movie.getReleaseDate().isAfter(LocalDate.now())) {
                                sb.append("状态：即将上映\n");
                            } else {
                                sb.append("状态：正在热映\n");
                            }
                        }
                        if (movie.getDuration() != null) {
                            sb.append("时长�?).append(movie.getDuration()).append("分钟\n");
                        }
                        if (movie.getDescription() != null) {
                            sb.append("简介：").append(movie.getDescription()).append("\n");
                        }
                        return Document.from(sb.toString());
                    })
                    .collect(Collectors.toList());
            documents.addAll(movieDocs);
            log.info("已加载{}部电影数�?, movieDocs.size());
        }

        List<Cinema> cinemas = cinemaService.listByStatus(null);
        if (cinemas != null && !cinemas.isEmpty()) {
            List<Document> cinemaDocs = cinemas.stream()
                    .filter(cinema -> cinema.getStatus() != null && cinema.getStatus() == 1)
                    .map(cinema -> {
                        StringBuilder sb = new StringBuilder();
                        sb.append("类型：影院\n");
                        sb.append("影院ID�?).append(cinema.getId()).append("\n");
                        sb.append("影院名称�?).append(cinema.getName()).append("\n");
                        if (cinema.getAddress() != null) {
                            sb.append("地址�?).append(cinema.getAddress()).append("\n");
                        }
                        if (cinema.getPhone() != null) {
                            sb.append("电话�?).append(cinema.getPhone()).append("\n");
                        }
                        if (cinema.getDescription() != null) {
                            sb.append("简介：").append(cinema.getDescription()).append("\n");
                        }
                        return Document.from(sb.toString());
                    })
                    .collect(Collectors.toList());
            documents.addAll(cinemaDocs);
            log.info("已加载{}家影院数�?, cinemaDocs.size());
        }

        documents.removeIf(doc -> doc.text().trim().isEmpty());

        List<Document> chunkedDocs = new ArrayList<>();
        for (Document doc : documents) {
            String text = doc.text();
            int chunkSize = 500;
            for (int i = 0; i < text.length(); i += chunkSize) {
                int end = Math.min(text.length(), i + chunkSize);
                chunkedDocs.add(Document.from(text.substring(i, end)));
            }
        }

        InMemoryEmbeddingStore store = new InMemoryEmbeddingStore();
        EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                .embeddingModel(embeddingModel)
                .build()
                .ingest(chunkedDocs);

        log.info("向量存储构建完成，共{}个文档块", chunkedDocs.size());
        
        return store;
    }

    @Bean
    @Lazy
    public ContentRetriever contentRetriever(
            EmbeddingStore embeddingStore,
            EmbeddingModel embeddingModel) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .minScore(0.6)
                .maxResults(5)
                .build();
    }
}
