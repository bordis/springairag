package br.com.juliano.springairag.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@ConfigurationProperties(prefix = "sfg.aiapp")
public class VectorStoreProperties {

    private String vectorStorePath;
    private List<Resource> getDocumentsToLoad;

    // Getters and Setters
    public String getVectorStorePath() {
        return vectorStorePath;
    }

    public void setVectorStorePath(String vectorStorePath) {
        this.vectorStorePath = vectorStorePath;
    }

    public List<Resource> getDocumentsToLoad() {
        return getDocumentsToLoad;
    }

    public void setDocumentsToLoad(List<Resource> getDocumentsToLoad) {
        this.getDocumentsToLoad = getDocumentsToLoad;
    }

}
