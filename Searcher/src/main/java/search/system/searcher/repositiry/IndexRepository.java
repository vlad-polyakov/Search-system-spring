package search.system.searcher.repositiry;

import org.springframework.data.mongodb.repository.MongoRepository;
import search.system.searcher.model.Index;

import java.util.List;

public interface IndexRepository extends MongoRepository<Index, String> {
    List<Index> findByTermin(String termin);
}
