package anagramms_claude_opus_4_5;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TwoWordAnagramFinderTest {

    @Test
    void shouldFindAnagramsForDocumenting() throws IOException {
        TwoWordAnagramFinder finder = TwoWordAnagramFinder.fromFile(Path.of("wordlist.txt"));

        List<TwoWordAnagramFinder.WordPair> anagrams = finder.findTwoWordAnagrams("documenting");

        assertFalse(anagrams.isEmpty(), "Sollte Anagramme für 'documenting' finden");
        assertTrue(anagrams.stream().allMatch(pair -> isAnagram(pair.first() + pair.second(), "documenting")),
                "Alle gefundenen Paare sollten Anagramme sein");
    }

    @Test
    void shouldFindNoAnagramsWhenNoMatchExists() {
        Set<String> wordList = Set.of("hello", "world");
        TwoWordAnagramFinder finder = new TwoWordAnagramFinder(wordList);

        List<TwoWordAnagramFinder.WordPair> anagrams = finder.findTwoWordAnagrams("xyz");

        assertTrue(anagrams.isEmpty(), "Sollte keine Anagramme finden");
    }

    @Test
    void shouldHandleSimpleCase() {
        Set<String> wordList = Set.of("act", "cat", "dog", "god");
        TwoWordAnagramFinder finder = new TwoWordAnagramFinder(wordList);

        List<TwoWordAnagramFinder.WordPair> anagrams = finder.findTwoWordAnagrams("catdog");

        assertFalse(anagrams.isEmpty(), "Sollte 'cat dog' oder 'act god' finden");
        assertTrue(anagrams.stream().anyMatch(pair ->
                (pair.first().equals("cat") && pair.second().equals("dog")) ||
                (pair.first().equals("act") && pair.second().equals("god"))),
                "Sollte gültige Anagramm-Paare enthalten");
    }

    @Test
    void shouldIgnoreCaseDifferences() {
        Set<String> wordList = Set.of("ABC", "def");
        TwoWordAnagramFinder finder = new TwoWordAnagramFinder(wordList);

        List<TwoWordAnagramFinder.WordPair> anagrams = finder.findTwoWordAnagrams("ABCDEF");

        assertFalse(anagrams.isEmpty(), "Sollte Anagramme unabhängig von Groß-/Kleinschreibung finden");
    }

    private boolean isAnagram(String s1, String s2) {
        String normalized1 = s1.toLowerCase().chars().sorted()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        String normalized2 = s2.toLowerCase().chars().sorted()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        return normalized1.equals(normalized2);
    }
}

