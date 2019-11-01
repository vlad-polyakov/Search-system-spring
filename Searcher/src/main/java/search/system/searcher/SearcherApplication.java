package search.system.searcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import search.system.searcher.model.Documents;
import search.system.searcher.repositiry.DocumentsRepository;

@SpringBootApplication
public class SearcherApplication implements CommandLineRunner{
    
    @Autowired
    private DocumentsRepository documentsRepository;
	public static void main(String[] args) {
		SpringApplication.run(SearcherApplication.class, args);
	}
	
	@Override
    public void run(String... args) throws Exception {
	    documentsRepository.deleteAll();
	    Documents documents = new Documents("Gomel", 50);
	    documents.setId((long) 123456789);
	    documentsRepository.save(documents);
    }
}
