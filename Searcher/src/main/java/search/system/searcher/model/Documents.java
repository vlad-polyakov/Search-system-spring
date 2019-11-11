package search.system.searcher.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.experimental.var;

@Data
@Document
public class Documents {

    @Id
    private String id;
    private String name;
    private List<String> words;
    private String snippet;

    public Documents() {
    }


    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public boolean findTermin(String termin) {
        for(String word: this.words) {
            if(word.equals(termin)) return true;
        }
        return false;
    }

    public double frequencyOfTermin(String termin) {
        double frequency = 0;
        for(String word: this.words) {
            if(word.equals(termin)) frequency++;
        }

        frequency /= words.size();
        return frequency;
    }



}
