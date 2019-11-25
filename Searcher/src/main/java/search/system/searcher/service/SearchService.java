package search.system.searcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import search.system.searcher.controller.DocumentsController;
import search.system.searcher.controller.IndexController;
import search.system.searcher.controller.SearchController;
import search.system.searcher.model.Documents;
import search.system.searcher.model.Index;
import search.system.searcher.model.SearchResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    private DocumentsService documentsService;

    @Autowired
    private DocumentsController documentsController;

    @Autowired
    private IndexController indexController;

    @Autowired
    private SearchController searchController;

    @Autowired
    private SnippetService snippetService;

    public List<SearchResult> search(String query) {
        Map<String, List<String>> result = new HashMap<>();
        List<String> words = documentsService.getWordsFromString(query);
        for(String word: words) {
            List<Index> index = indexController.get(word);
            if(index != null) {
                for(Index el: index) {
                    String documentId = el.getDocumentId();
                    List<String> wordsFromQuery = result.get(documentId);
                    if(wordsFromQuery == null) {
                        List<String> newListWords = new ArrayList<>();
                        newListWords.add(word);
                        result.put(documentId, newListWords);
                    }
                    else {
                       result.get(documentId).add(word);
                    }
                }
            }
        }
        List<SearchResult> results = makeSearchResult(result, words.size());
        return results;
    }

    private List<SearchResult> makeSearchResult(Map<String, List<String>> result, int queryWordsNumber) {
        List<SearchResult> searchResults = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            String documentId = entry.getKey();
            List<String> foundKeywordValues = entry.getValue();

            Documents document = documentsController.getById(documentId).get();
            String filePath = document.getPath();
            File file = new File(filePath);
            String documentText = textFromFile(file).toLowerCase();
            String documentSnippet = snippetService.search(documentText, foundKeywordValues);

            double documentRank = (double)foundKeywordValues.size()/(double)queryWordsNumber;

            SearchResult searchResult = new SearchResult(
                    document.getName(), documentSnippet, documentRank, filePath, document.getAddingDate()
            );
            searchResults.add(searchResult);
        }
        List<SearchResult> sortedResults = searchResults.stream()
                .sorted(Comparator.comparing(SearchResult::getRank).reversed())
                .collect(Collectors.toList());
        for(SearchResult searchResult: sortedResults) {
            searchController.post(searchResult);
        }
        return sortedResults;
    }
    public String textFromFile(File file) {
        String text = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null)
                text += st;
        }
        catch (IOException ex) {}
        return text;
    }

}
