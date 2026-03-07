package com.deevyanshu.advisor.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final ChatClient.Builder clientBuilder;

    private VectorStore vectorStore;

    public ChatService(VectorStore vectorStore, ChatClient.Builder clientBuilder) {

        this.clientBuilder=clientBuilder;
        this.chatClient=clientBuilder.defaultOptions(GoogleGenAiChatOptions.builder()
                .model("gemini-3-flash-preview").temperature(0.2).maxOutputTokens(1000).build())
                .build();
        this.vectorStore=vectorStore;
    }

    public String chatTemplate(String q)
    {

        //Advance rag flow
        var retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                //pre-retrieval
                .queryTransformers(RewriteQueryTransformer.builder()
                        .chatClientBuilder(clientBuilder).build())
                .queryExpander(MultiQueryExpander.builder().chatClientBuilder(clientBuilder).numberOfQueries(2)
                        .build())
                //retrieval
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore).
                topK(3).similarityThreshold(0.5)
                        .build())
                .documentJoiner(new ConcatenationDocumentJoiner())
                //post-retrieval and generation
                .queryAugmenter(ContextualQueryAugmenter.builder().build())
                .build();

        return chatClient.prompt().advisors(retrievalAugmentationAdvisor).user(q).call().content();

    }



    //for dumping data to vector database
    public void saveData(List<String> list)
    {
        List<Document> documentList=list.stream().map(item->new Document(item)).collect(Collectors.toList());
        this.vectorStore.add(documentList);
    }
}
