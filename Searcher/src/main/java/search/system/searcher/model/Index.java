package search.system.searcher.model;

import lombok.Data;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;
import java.util.Map;

@Data
@Document
    public class Index {
        @Id
        private String id;
        private String termin;
        private String documentId;

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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}


