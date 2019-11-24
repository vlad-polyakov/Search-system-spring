package search.system.searcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import search.system.searcher.model.Documents;
import search.system.searcher.model.Index;
import search.system.searcher.repositiry.IndexRepository;

import java.util.List;

@RestController
public class IndexController {

    @Autowired
    private IndexRepository indexRepository;

    @PostMapping("index")
    public ResponseEntity<Index> post(@RequestBody Index index) {
        if (index == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Index saved = indexRepository.save(index);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    @GetMapping("index")
    public List<Index> get(@RequestBody String termin) {
        List<Index> list = indexRepository.findByTermin(termin);
        System.out.println(list);
        return indexRepository.findByTermin(termin);
    }


}
