package br.com.juliano.springairag.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliano.springairag.model.Answer;
import br.com.juliano.springairag.model.Question;
import br.com.juliano.springairag.services.OllamaAIService;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "*")
public class OllamaAIController {

    
    
    @Autowired
    private OllamaAIService openAIService;

    // question
    @PostMapping("/question")
    public ResponseEntity<Answer> ask(@RequestBody Question question) {
        Answer answer = openAIService.getAnswer(question);
        return ResponseEntity.ok(answer);
    }

}
