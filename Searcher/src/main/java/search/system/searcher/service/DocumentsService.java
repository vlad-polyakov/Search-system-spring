package search.system.searcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import search.system.searcher.controller.DocumentsController;
import search.system.searcher.controller.IndexController;
import search.system.searcher.model.Constants;
import search.system.searcher.model.Documents;
import search.system.searcher.model.Index;

import javax.print.Doc;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DocumentsService {

    @Autowired
    private DocumentsController documentsController;

    @Autowired
    private IndexController indexController;
    public void setIndex() {
		addDocumentsWithWords("d:\\folder");
		List<Documents> documents = documentsController.get();
		List<Index> indexes = new ArrayList<>();
        List<Index> keyTermins = filterWordsInDocumentByWeight(documents);
		for(Documents document: documents){

        }



	}

	public void addDocumentsWithWords(String pathName) {
        List<Documents> documents = new ArrayList<>();
        File folder = new File(pathName);
        File[] files = folder.listFiles();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource("stop.txt");
            List<String> stopwords = Files.readAllLines(Paths.get(resource.getPath().substring(1)));
            for (File file : files) {
                String text = "";
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null)
                    text += st;
                List<String> words = new ArrayList<>();
                StringTokenizer token = new StringTokenizer(text, " \t\n\r,.");
                while(token.hasMoreTokens()){
                    String word = token.nextToken().toLowerCase();
                    word = word.replaceAll("[\\s\t\n\r\\[\\]«».\\d?—!:;*#<>]", "");
                    if(word.length()>1) {
                        words.add(word);
                    }
                }
                BasicFileAttributes fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                Date creationDate =
                        new Date(fileAttributes.creationTime().to(TimeUnit.MILLISECONDS));
                Documents document = new Documents(null, file.getName(), creationDate, words, file.getPath());
                document.setName(file.getName());
                document.setWords(words);
                words.removeAll(stopwords);
                documents.add(document);
                documentsController.post(document);
            }
        }
        catch(IOException exception){
            System.out.println("Some problems...");
        }
    }


    public List<Index> filterWordsInDocumentByWeight(List<Documents> documents) {
        List<Index> listWithTermins = new ArrayList<>();
        for (Documents document: documents) {
            Map<String, Double> terminsAndtheirWeights = getTerminsWithWeights(document,documents);
            List<String> newWords = terminsAndtheirWeights.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() > Constants.minWeight && entry.getValue() < Constants.maxWeight)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            if(newWords.size() != 0) {
                for(String newWord: newWords) {
                    Index index = new Index();
                    index.setDocumentId(document.getId());
                    index.setTermin(newWord);
                    listWithTermins.add(index);
                    indexController.post(index);
                }
            }

        }
        return listWithTermins;
    }

    public double getCountOfFilesWithWord(String termin, List<Documents> documents) {
        double count = 0;
        for(Documents document: documents) {
            for(String word: document.getWords()) {
                if (word.equals(termin)){
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    public Map<String,Double> getTerminsWithWeights(Documents document, List<Documents> documentsList) {
        Map<String,Double> termsWeight = new HashMap<>();
        for(String word: document.getWords()) {
            double invertedFrequencyOfTermin = Math.log10((double)documentsList.size()/getCountOfFilesWithWord(word, documentsList));
            double terminsWeightIndDocument = invertedFrequencyOfTermin * getTerminsFrequency(document.getWords(), word);
            System.out.println(word + " " + terminsWeightIndDocument);
            termsWeight.put(word, terminsWeightIndDocument);
        }
        double sum = 0;
       /* for(Map.Entry<String, Double> entry : termsWeight.entrySet()) {
            sum += (entry.getValue()* entry.getValue());
            }
        for(Map.Entry<String, Double> entry : termsWeight.entrySet()) {
            double normalWeightOfWord = entry.getValue()/ Math.sqrt(sum);
            normalWeight.put(entry.getKey(), normalWeightOfWord);
            System.out.println(entry.getKey()+ " " +normalWeightOfWord);
        }*/
        return termsWeight;
    }

    public double getTerminsFrequency(List<String> words, String termin) {
        double count = 0;
        for(String word: words) {
            if(word.equals(termin)) count++;
        }
        return count;
    }




}
