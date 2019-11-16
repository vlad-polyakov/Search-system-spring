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
import search.system.searcher.service.DocumentsService;

import javax.print.Doc;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@SpringBootApplication
public class SearcherApplication implements CommandLineRunner{

    @Autowired
    private DocumentsRepository documentsRepository;

	@Autowired
	private IndexRepository indexRepository;

	@Autowired
	private DocumentsController documentsController;

	@Autowired
	private DocumentsService documentsService;
	public static void main(String[] args) {
		SpringApplication.run(SearcherApplication.class, args);
	}
	
	@Override
    public void run(String... args) throws Exception {
		documentsRepository.deleteAll();
		indexRepository.deleteAll();
		documentsService.setIndex();
	   // documentsRepository.deleteAll();
	   // indexRepository.deleteAll();
	   // setIndex();
    }




}
