package br.com.juliano.springairag.services;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import br.com.juliano.springairag.model.Answer;
import br.com.juliano.springairag.model.Question;

@Service
public class OllamaAIService {

    final ChatModel chatModel;
    final SimpleVectorStore vectorStore;

    // Constructor injection for better testability and immutability
    public OllamaAIService(ChatModel chatModel, SimpleVectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @Value("classpath:/templates/rag.st")
    private Resource ragPromptTemplate;

    public Answer getAnswer(Question question) {

        Message systemMessage = createSystemMessage(question.question());

        Message userMessage = new UserMessage(question.question());

        Prompt prompt = new Prompt(systemMessage, userMessage);

        System.out.println(prompt);

        ChatResponse chatResponse = chatModel.call(prompt);

        return new Answer(chatResponse.getResult().getOutput().getContent());

    }

    private Message createSystemMessage(String userMessageText) {

        SearchRequest searchRequest = SearchRequest.query(userMessageText).withTopK(3);

        List<Document> documents = vectorStore.similaritySearch(searchRequest);

        List<String> contentList = documents.stream().map(Document::getContent).toList();

        return new SystemPromptTemplate(ragPromptTemplate)
                .createMessage(Map.of("input", userMessageText, "documents", String.join("\n", contentList)));
    }

}
