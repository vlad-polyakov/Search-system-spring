package search.system.searcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import search.system.searcher.controller.DocumentsController;
import search.system.searcher.model.Documents;
import search.system.searcher.model.Index;
import search.system.searcher.repositiry.DocumentsRepository;
import search.system.searcher.repositiry.IndexRepository;

import javax.print.Doc;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@SpringBootApplication
public class SearcherApplication implements CommandLineRunner{

    @Autowired
    private DocumentsRepository documentsRepository;

	@Autowired
	private IndexRepository indexRepository;

	public static void main(String[] args) {
		SpringApplication.run(SearcherApplication.class, args);
	}
	
	@Override
    public void run(String... args) throws Exception {
	    documentsRepository.deleteAll();
	    indexRepository.deleteAll();
	    setIndex();
    }

	public void setIndex() {
		List<Documents> documents = new ArrayList<>();
		List<Index> indexes = new ArrayList<>();
		File myFolder = new File("d:\\folder");
		File[] files = myFolder.listFiles();
		SortedSet<String> termins = new TreeSet<>();
		try {
			List<String> stopwords = Files.readAllLines(Paths.get("D:\\Search-system\\Searcher\\src\\main\\resources\\stop.txt"));
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
					word = word.replaceAll("[\\s\t\n\r\\[\\]«».\\d?—!]", "");
					if(word.length()>1) {
						termins.add(word);
						words.add(word);
					}
				}
				Documents document = new Documents();
				document.setName(file.getName());
				document.setWords(words);
				words.removeAll(stopwords);
				termins.removeAll(stopwords);
				documents.add(document);
			}
		}
		catch(IOException exception){
		}
		for(String termin: termins) {
			Index indexx = new Index();
			indexx.setTermin(termin);
			indexes.add(indexx);
		}

		for(Index indexx: indexes) {
			String termin = indexx.getTermin();
			int countWithTermin = 0;
			Map<String, Documents> weightCoeffsForDocs = new HashMap<>();
			double iter = 0;
			List<Documents> docs = new ArrayList<>();
			for(Documents document: documents) {
				if(document.findTermin(termin)) {
					countWithTermin++;
					docs.add(document);
					iter++;
				}
			}
			double frequency = Math.log10(documents.size()/countWithTermin);
			for(Documents doc: docs){
				double weight = frequency * doc.frequencyOfTermin(termin);
				weightCoeffsForDocs.put(String.valueOf(weight).replace(".", ","), doc);
			}
			indexx.setWeightCoeffsForDocs(weightCoeffsForDocs);
		}
		for(Index indexx: indexes) {
			if(indexx.getTermin().equals("")) indexes.remove(indexx);
		}
		for(Index indexx: indexes) {
			indexRepository.save(indexx);
			for(Map.Entry<String, Documents> entry: indexx.getWeightCoeffsForDocs().entrySet()){
				System.out.println(indexx.getTermin()+" " + entry.getValue().getName() + " " + entry.getKey());
			}
		}

		for( Documents document: documents) documentsRepository.save(document);

	}


}
