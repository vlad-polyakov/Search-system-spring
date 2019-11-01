package search.system.searcher.repositiry;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import search.system.searcher.model.Documents;


public interface DocumentsRepository extends MongoRepository<Documents, String> {

}
