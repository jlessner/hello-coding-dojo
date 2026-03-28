package two_word_anagrams;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TwoWordAnagramsTest {

    @Test
    void findetAnagrammeFuerDocumenting() throws IOException {
        List<String> words = TwoWordAnagrams.readWords(Path.of("wordlist.txt"));
        List<String[]> results = TwoWordAnagrams.findTwoWordAnagrams("documenting", words);
        assertFalse(results.isEmpty(), "Es sollten Anagramme gefunden werden");
        results.forEach(pair -> assertEquals(11, (pair[0] + pair[1]).length()));
    }

    @Test
    void kombinierteZeichenSindAnagramm() throws IOException {
        List<String> words = TwoWordAnagrams.readWords(Path.of("wordlist.txt"));
        List<String[]> results = TwoWordAnagrams.findTwoWordAnagrams("documenting", words);
        for (String[] pair : results) {
            String combined = pair[0] + pair[1];
            assertEquals(sortedChars("documenting"), sortedChars(combined),
                    pair[0] + " + " + pair[1] + " ist kein Anagramm von 'documenting'");
        }
    }

    @Test
    void keinAnagrammFuerUnmoeglicherString() {
        List<String[]> results = TwoWordAnagrams.findTwoWordAnagrams("xyz", List.of("ab", "cd"));
        assertTrue(results.isEmpty());
    }

    private String sortedChars(String s) {
        char[] chars = s.toLowerCase().toCharArray();
        java.util.Arrays.sort(chars);
        return new String(chars);
    }
}
