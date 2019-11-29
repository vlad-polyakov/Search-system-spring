package search.system.searcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import search.system.searcher.model.Documents;
import search.system.searcher.model.Index;
import search.system.searcher.model.Metric;
import search.system.searcher.model.SearchResult;
import search.system.searcher.repositiry.SearchResultRepository;
import search.system.searcher.service.MetricService;
import search.system.searcher.service.SearchService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SearchController implements ErrorController {
    private static final String PATH = "/error";
    @Autowired
    private SearchService searchService;

    @Autowired
    private DocumentsController documentsController;

    @Autowired
    private MetricService metricService;

    @Autowired
    private SearchResultRepository searchResultRepository;
    @RequestMapping(value = PATH)
    public String error() {
        return "Error handling";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    @RequestMapping(value = "search", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<Metric, List<SearchResult>>> get(@RequestParam(name = "query") String query ) {
        Map<Metric, List<SearchResult>> result = new HashMap<>();

        List<SearchResult> searchResults = searchService.search(query);
        int a =searchResults.size();
        int b = 0;
        int c = documentsController.getDocumentsNumber();
        int d = 0;
        result.put(metricService.generateMetric(a,b,c,d), searchResults);
        post(metricService.generateMetric(a,b,c,d));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("metric")
    public List<Metric> get() {
        return searchResultRepository.findAll();
    }


    @PostMapping("metric")
    public ResponseEntity<Metric> post(@RequestBody Metric index) {
        if (index == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Metric saved = searchResultRepository.save(index);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
