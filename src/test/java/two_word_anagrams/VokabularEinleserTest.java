package two_word_anagrams;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VokabularEinleserTest {

    @Test
    void liesWoerterAusDateiMitMehrerenWoerternProZeile(@TempDir Path tempDir) throws IOException {
        Path datei = tempDir.resolve("woerter.txt");
        Files.writeString(datei, "Peter Paul    Mary\nJohn  Jane\n");

        List<Wort> vokabular = new VokabularEinleser().liesVokabular(datei);

        assertEquals(List.of(new Wort("Peter"), new Wort("Paul"), new Wort("Mary"),
                new Wort("John"), new Wort("Jane")), vokabular);
    }

    @Test
    void wirftExceptionBeiWenigerAlsZweiWoertern(@TempDir Path tempDir) throws IOException {
        Path datei = tempDir.resolve("einzelwort.txt");
        Files.writeString(datei, "einziges\n");

        assertThrows(IllegalStateException.class,
                () -> new VokabularEinleser().liesVokabular(datei));
    }
}
