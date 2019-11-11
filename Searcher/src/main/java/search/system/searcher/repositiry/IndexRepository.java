package search.system.searcher.repositiry;

import org.springframework.data.mongodb.repository.MongoRepository;
import search.system.searcher.model.Documents;
import search.system.searcher.model.Index;

public interface IndexRepository extends MongoRepository<Index, String> {
}
