package search.system.searcher.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SnippetService {

    public String search(String text, List<String> keywordValues) {
        System.out.println(keywordValues);
        List<String> remainingKeywordValues = new ArrayList<>(keywordValues);

        StringBuilder snippet = new StringBuilder();
        for (String keywordValue : keywordValues) {
            if (remainingKeywordValues.contains(keywordValue)) {
                Optional<String> restOfSentenceWithWord = findRestOfSentenceWithWord(text, keywordValue);
                restOfSentenceWithWord.ifPresent(snippetPart -> {
                    remainingKeywordValues.removeIf(
                            nextKeywordValue -> findRestOfSentenceWithWord(snippetPart, nextKeywordValue).isPresent()
                    );
                    snippet.append("...").append(snippetPart).append("\n");
                });
            }
        }

        return snippet.toString();
    }

    private Optional<String> findRestOfSentenceWithWord(String text, String keywordValue) {
        Pattern pattern = Pattern.compile(
                "(\"*«*" + keywordValue + ",*\"*»* .*?[.!?])|(" + keywordValue + "[.!?])",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? Optional.of(matcher.group()) : Optional.empty();
    }
}

