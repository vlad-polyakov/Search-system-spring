package search.system.searcher.repositiry;

import org.springframework.data.mongodb.repository.MongoRepository;
import search.system.searcher.model.Index;
import search.system.searcher.model.SearchResult;

public interface SearchResultRepository extends MongoRepository<SearchResult, String> {
}
