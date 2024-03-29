package search.system.searcher.service;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
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
import java.util.regex.Pattern;
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
        List<SearchResult> results = new ArrayList<>();
        List<String> words = getPartsOfQuery(query);
        System.out.println(words.size());
        int addToSize = 0;
        for(String word: words) {
            System.out.println(word);
            boolean not = false;
            boolean and = false;
            if (word.contains("!") && !word.contains("and")) {
                not = true;
                word=word.substring(1);
            }
            List<Index> index = new ArrayList<>();
            if(word.contains("and")) {
                and = true;
                result = getResultOfAND(result,word,index);
                addToSize++;
            }
            else index = indexController.get(word);
            if (not) {
                result = getResultOfNO(index, word, result);
            } else {
                if (index != null && !and) {
                    for (Index el : index) {
                        String documentId = el.getDocumentId();
                        List<String> wordsFromQuery = result.get(documentId);
                        if (wordsFromQuery == null) {
                            List<String> newListWords = new ArrayList<>();
                            newListWords.add(word);
                            result.put(documentId, newListWords);
                        } else {
                            result.get(documentId).add(word);
                        }
                    }
                }
            }
        }
        results = makeSearchResult(result, words.size()+addToSize);
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
    public List<String> getPartsOfQuery(String text) {
        List<String> words = new ArrayList<>();
        boolean hasAND = false;
        if(text.contains("AND")) {
            text = text.replace(" AND ", "AND");
            hasAND = true;
        }
        StringTokenizer token = new StringTokenizer(text, " \t\n\r,.");
        while(token.hasMoreTokens()){
            String word = token.nextToken().toLowerCase();
            if(word.matches("!\\w*[А-Я]*[а-я]*")) {
                word = "!"+word.replaceAll("[\\s\t\n\r\\[\\]«».?—!:;*#<>…]", "");
            }
            else if(!hasAND) word = word.replaceAll("[\\s\t\n\r\\[\\]«».?—!:;*#<>…]", "");
            if(word.length()>1) {
                words.add(word);
            }
        }
        return words;
    }

    public Map<String, List<String>> getResultOfNO(List<Index> index, String word, Map<String, List<String>> resultMap) {
        if (index != null && index.size() != documentsController.getDocumentsNumber()) {
            List<Documents> documents = documentsController.get();
            List<Documents> unsuitableDocuments = new ArrayList<>();
            for (Documents document : documents) {
                for (Index oneIndex : index) {
                    if (document.getId().equals(oneIndex.getDocumentId())) unsuitableDocuments.add(document);
                }
            }
            documents.removeAll(unsuitableDocuments);
            for (Documents document : documents) {
                System.out.println(document.getName());
                if (resultMap.get(document.getId()) == null) {
                    List<String> newListWords = new ArrayList<>();
                    newListWords.add(word);
                    resultMap.put(document.getId(), newListWords);
                } else {
                    resultMap.get(document.getId()).add(word);
                }
            }
        }
        return resultMap;
    }

    public Map<String, List<String>> getResultOfAND(Map<String, List<String>> result, String word, List<Index> index) {
        Map<String, List<String>> andMap = new HashMap<>();
        word = word.replace("and", " ");
        System.out.println("AND: " + word);
        String NOword = "";
        List<String> andWords = getPartsOfQuery(word);
        for(String littleWord: andWords) {
            System.out.println(littleWord);
            if(littleWord.contains("!")) {
                littleWord = littleWord.replace("!", "");
                List<Index> noIndex = indexController.get(littleWord);
                andMap = getResultOfNO(noIndex, littleWord, andMap);
            }
            else index.addAll(indexController.get(littleWord));
        }
        for(Index ind: index) {
            String id = ind.getDocumentId();
            if (andMap.get(id) == null) {
                List<String> newListWords = new ArrayList<>();
                newListWords.add(ind.getTermin());
                andMap.put(id, newListWords);
            } else {
                andMap.get(id).add(ind.getTermin());
            }
        }
        System.out.println(result);
        System.out.println(andMap);
        for(Map.Entry<String, List<String>> entry : andMap.entrySet()) {

            if(entry.getValue().size() == andWords.size()) {
                boolean here = false;
                System.out.println(andMap);
                String key = "";
                for(Map.Entry<String, List<String>> newEntry : result.entrySet()) {
                    if (entry.getKey().equals(newEntry.getKey())) {
                        key = entry.getKey();
                        result.get(key).addAll(entry.getValue());
                    }
                }
                if(key.equals("")) result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }







}
