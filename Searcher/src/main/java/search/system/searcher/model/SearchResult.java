package search.system.searcher.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Document
public class SearchResult {
    private String title;
    private String snippet;
    private double rank;
    private String filePath;
    private Date addingTime;

    public SearchResult(String title, String snippet, double rank, String filePath, Date addingTime) {
        this.title = title;
        this.snippet = snippet;
        this.rank = rank;
        this.filePath = filePath;
        this.addingTime = addingTime;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public double getRank() {
        return rank;
    }

    public String getFilePath() {
        return filePath;
    }

    public Date getAddingTime() {
        return addingTime;
    }


}
