package search.system.searcher.repositiry;

import org.springframework.data.mongodb.repository.MongoRepository;
import search.system.searcher.model.Documents;

public interface FakeDocumentsRepository extends MongoRepository<Documents, String> {
}
