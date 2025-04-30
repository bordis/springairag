package br.com.juliano.springairag.config;

import java.io.File;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class VectorStoreConfig {

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel,
            VectorStoreProperties vectorStoreProperties) {

        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingModel);

        File vectorStoreFile = new File(vectorStoreProperties.getVectorStorePath());

        if (vectorStoreFile.exists()) {
            simpleVectorStore.load(vectorStoreFile);
        } else {
            log.debug("Loading vector store from scratch...");
            vectorStoreProperties.getDocumentsToLoad().forEach(doc -> {
                log.debug("Saving vector store to " + doc.getFilename() + "...");
                TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(doc);
                List<Document> docs = tikaDocumentReader.get();
            
                //FILTRA documentos com conteúdo nulo ou vazio
                List<Document> validDocs = docs.stream()
                    .filter(d -> d.getContent() != null && !d.getContent().isBlank())
                    .toList();
            
                if (validDocs.isEmpty()) {
                    log.warn("Documento ignorado por estar vazio: " + doc.getFilename());
                    return;
                }
            
                TextSplitter textSplitter = new TokenTextSplitter();
                List<Document> splitDocs = textSplitter.apply(validDocs);
                simpleVectorStore.add(splitDocs);
            });

            simpleVectorStore.save(vectorStoreFile);
        }

        return simpleVectorStore;

    }

}
