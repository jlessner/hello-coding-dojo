package two_word_anagrams;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ZweiwortAnagrammFinderTest {

    @Test
    void findetAnagrammDocumentUndGin(@TempDir Path tempDir) throws IOException {
        Path datei = tempDir.resolve("wordlist.txt");
        Files.writeString(datei, "document gin\n");

        List<WortPaar> anagramme = new ZweiwortAnagrammFinder().finde(datei);

        Wort document = new Wort("document");
        Wort gin = new Wort("gin");
        assertTrue(anagramme.stream().anyMatch(p ->
                p.erstesWort().equals(document) && p.zweitesWort().equals(gin)
                || p.erstesWort().equals(gin) && p.zweitesWort().equals(document)));
    }

    @Test
    void findetKeineAnagrammeFuerUngeeigneteWoerter(@TempDir Path tempDir) throws IOException {
        Path datei = tempDir.resolve("wordlist.txt");
        Files.writeString(datei, "alpha beta\n");

        List<WortPaar> anagramme = new ZweiwortAnagrammFinder().finde(datei);

        assertTrue(anagramme.isEmpty());
    }
}
