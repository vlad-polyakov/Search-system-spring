package search.system.searcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import search.system.searcher.model.Documents;
import search.system.searcher.repositiry.DocumentsRepository;

@RestController
public class DocumentsController {
    @Autowired
    private DocumentsRepository documentsRepository;
    @PostMapping("documents")
    public ResponseEntity<Documents> post(@RequestBody Documents documents) {
        documents.setName("Gomel city");
        documents.setPopulation(500000);
        if (documents == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
       
        Documents saved = documentsRepository.save(documents);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
