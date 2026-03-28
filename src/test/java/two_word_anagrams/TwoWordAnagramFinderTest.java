package two_word_anagrams;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TwoWordAnagramFinderTest {

    private final TwoWordAnagramFinder finder = new TwoWordAnagramFinder();

    @Test
    void findsSimpleAnagramPair() {
        List<String> words = List.of("coding", "ument", "other");
        // "coding" + "ument" = c,o,d,i,n,g,u,m,e,n,t = "documenting"
        List<WordPair> results = finder.find("documenting", words);

        assertEquals(1, results.size());
        assertPairContains(results.get(0), "coding", "ument");
    }

    @Test
    void noDuplicatePairs() {
        List<String> words = List.of("coding", "ument");
        List<WordPair> results = finder.find("documenting", words);

        assertEquals(1, results.size());
    }

    @Test
    void noResultWhenNoMatchExists() {
        List<String> words = List.of("hello", "world", "java");
        List<WordPair> results = finder.find("documenting", words);

        assertTrue(results.isEmpty());
    }

    @Test
    void wordsLongerThanTargetAreSkipped() {
        List<String> words = List.of("documentinglonger", "other");
        List<WordPair> results = finder.find("documenting", words);

        assertTrue(results.isEmpty());
    }

    @Test
    void findsAnagramsFromRealWordlist(@TempDir Path tempDir) throws IOException {
        Path wordlistPath = Path.of("wordlist.txt");
        WordlistReader reader = new WordlistReader();
        List<String> words = reader.read(wordlistPath);

        List<WordPair> results = finder.find("documenting", words);

        assertTrue(
            results.stream().anyMatch(pair ->
                (pair.first().equals("document") && pair.second().equals("gin")) ||
                (pair.first().equals("gin") && pair.second().equals("document"))),
            "Expected to find 'document + gin' in results"
        );
    }

    private void assertPairContains(WordPair pair, String word1, String word2) {
        assertTrue(
            (pair.first().equals(word1) && pair.second().equals(word2)) ||
            (pair.first().equals(word2) && pair.second().equals(word1)),
            "Expected pair (" + word1 + ", " + word2 + ") but got (" + pair.first() + ", " + pair.second() + ")"
        );
    }
}
