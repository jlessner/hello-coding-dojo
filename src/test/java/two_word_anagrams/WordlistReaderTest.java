package two_word_anagrams;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordlistReaderTest {

    private final WordlistReader reader = new WordlistReader();

    @Test
    void readsWordsFromFile(@TempDir Path tempDir) throws IOException {
        Path tmp = tempDir.resolve("wordlist.txt");
        Files.writeString(tmp, "Hello  World\nFoo   Bar");

        List<String> words = reader.read(tmp);

        assertEquals(List.of("hello", "world", "foo", "bar"), words);
    }

    @Test
    void normalizesToLowercase(@TempDir Path tempDir) throws IOException {
        Path tmp = tempDir.resolve("wordlist.txt");
        Files.writeString(tmp, "Apple BANANA Cherry");

        List<String> words = reader.read(tmp);

        assertEquals(List.of("apple", "banana", "cherry"), words);
    }

    @Test
    void handlesEmptyFile(@TempDir Path tempDir) throws IOException {
        Path tmp = tempDir.resolve("wordlist.txt");
        Files.writeString(tmp, "   ");

        List<String> words = reader.read(tmp);

        assertTrue(words.isEmpty());
    }
}
