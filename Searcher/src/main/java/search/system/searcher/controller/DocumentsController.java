package search.system.searcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import search.system.searcher.model.Documents;
import search.system.searcher.model.Index;
import search.system.searcher.repositiry.DocumentsRepository;
import search.system.searcher.repositiry.IndexRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
public class DocumentsController {
    @Autowired
    private DocumentsRepository documentsRepository;

    @Autowired
    private IndexRepository indexRepository;
    @PostMapping("documents")
    public ResponseEntity<Documents> post(@RequestBody Documents documents) {
        documents.setName("Gomel city");
        if (documents == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
       
        Documents saved = documentsRepository.save(documents);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("index")
    public ResponseEntity<Index> post(@RequestBody Index index) {

        if (index == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Index saved = indexRepository.save(index);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }


}
