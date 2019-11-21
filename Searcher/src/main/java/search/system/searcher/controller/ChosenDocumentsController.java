package search.system.searcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import search.system.searcher.model.Documents;
import search.system.searcher.repositiry.DocumentsRepository;
import search.system.searcher.repositiry.FakeDocumentsRepository;
import search.system.searcher.service.DocumentsService;
import search.system.searcher.service.FileService;

import java.util.List;

@RestController
public class ChosenDocumentsController {
    @Autowired
    private FakeDocumentsRepository documentsRepository;

    @Autowired
    private DocumentsService documentsService;
    @Autowired
    private FileService fileService;
    @CrossOrigin(origins = "http://localhost:4200")

    @PostMapping("fakeDocuments")
    public ResponseEntity<String> post(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            String filePath = fileService.store(file);
            documentsService.setIndex(filePath);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping("fakeDocuments")
    public List<Documents> get() {
        return documentsRepository.findAll();
    }
}