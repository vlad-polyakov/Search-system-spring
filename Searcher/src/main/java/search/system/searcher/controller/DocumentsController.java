package search.system.searcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import search.system.searcher.model.Documents;
import search.system.searcher.model.Index;
import search.system.searcher.repositiry.DocumentsRepository;
import search.system.searcher.repositiry.IndexRepository;
import search.system.searcher.service.DocumentsService;
import search.system.searcher.service.FileService;

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

    @PostMapping("documents")
    public ResponseEntity<Documents> post(@RequestBody Documents documents) {
        if (documents == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
       
        Documents saved = documentsRepository.save(documents);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("documents")
    public List<Documents> get() {
            return documentsRepository.findAll();
    }

    @GetMapping("id/documents")
    public Optional<Documents> getById(String id) {
        return documentsRepository.findById(id);
    }

    @PutMapping("documents")
    public Documents put(@RequestBody Documents document) {
        return documentsRepository.save(document);
    }

    public int getDocumentsNumber() {
        return get().size();
    }




}
