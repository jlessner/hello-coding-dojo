package two_word_anagrams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VokabularEinleser {

    public List<Wort> liesVokabular(Path dateiPfad) throws IOException {
        List<Wort> vokabular = new ArrayList<>();

        List<String> zeilen = Files.readAllLines(dateiPfad);
        for (String zeile : zeilen) {
            String[] woerter = zeile.trim().split("\\s+");
            for (String wort : woerter) {
                if (!wort.isEmpty()) {
                    vokabular.add(new Wort(wort));
                }
            }
        }

        if (vokabular.size() < 2) {
            throw new IllegalStateException("Weniger als zwei Wörter im Vokabular");
        }

        return vokabular;
    }
}
