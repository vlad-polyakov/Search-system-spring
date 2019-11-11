package search.system.searcher.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Map;

@Data
@Document
    public class Index {
        @Id
        private String id;
        private String termin;
        private Map<String, Documents> weightCoeffsForDocs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTermin() {
            return termin;
        }

        public void setTermin(String termin) {
            this.termin = termin;
        }

        public Map<String, Documents> getWeightCoeffsForDocs() {
            return weightCoeffsForDocs;
        }

        public void setWeightCoeffsForDocs(Map<String, Documents> weightCoeffsForDocs) {
            this.weightCoeffsForDocs = weightCoeffsForDocs;
        }
    }


