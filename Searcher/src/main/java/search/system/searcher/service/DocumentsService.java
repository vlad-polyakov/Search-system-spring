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
    public void setIndex(String path) {
		addDocumentsWithWords(path);
		List<Documents> documents = documentsController.get();
        List<Index> keyTermins = filterWordsInDocumentByWeight(documents);
	}

	public void addDocumentsWithWords(String pathName) {
        List<Documents> documents = new ArrayList<>();
        List<File> files = getFileList(pathName);
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
                   word = word.replaceAll("[\\s\t\n\r\\[\\]«».?—!:;*#<>…]", "");
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
        Map<String,Double> normalWeight = new HashMap<>();
        Map<String,Double> termsWeight = new HashMap<>();
        for(String word: document.getWords()) {
            double invertedFrequencyOfTermin = Math.log((double)documentsList.size()/getCountOfFilesWithWord(word, documentsList));
            double terminsWeightIndDocument = invertedFrequencyOfTermin * getTerminsFrequency(document.getWords(), word);
            System.out.println(word + " " + terminsWeightIndDocument);
            termsWeight.put(word, terminsWeightIndDocument);
        }
        double sum = 0;
        for(Map.Entry<String, Double> entry : termsWeight.entrySet()) {
            sum += Math.pow(entry.getValue(), 2);
            }
        for(Map.Entry<String, Double> entry : termsWeight.entrySet()) {
            double normalWeightOfWord = entry.getValue()/ Math.sqrt(sum);
            normalWeight.put(entry.getKey(), normalWeightOfWord);
        }
        return termsWeight;
    }

    public double getTerminsFrequency(List<String> words, String termin) {
        double count = 0;
        for(String word: words) {
            if(word.equals(termin)) count++;
        }
        return count/words.size();
    }

    public List<File> getFileList(String directoryName) {
        File directory = new File(directoryName);
        List<File> resultList = new ArrayList<>();
        File[] fList = directory.listFiles();
        resultList.addAll(Arrays.asList(fList));
        for (File file : fList) {
            if (file.isFile()) {
                System.out.println(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                resultList.addAll(getFileList(file.getAbsolutePath()));
            }
        }
        return resultList;
    }
}





