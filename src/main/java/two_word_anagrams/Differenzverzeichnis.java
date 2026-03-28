package two_word_anagrams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Differenzverzeichnis {

    private final Map<Buchstabendifferenz, List<Wort>> verzeichnis = new HashMap<>();

    public void erstelle(List<Wort> vokabular, Wort zielwort) {
        for (Wort wort : vokabular) {
            if (Buchstabendifferenz.istGeeignet(wort, zielwort)) {
                Buchstabendifferenz differenz = Buchstabendifferenz.vonWortGegenZielwort(wort, zielwort);
                verzeichnis.computeIfAbsent(differenz, k -> new ArrayList<>()).add(wort);
            }
        }
    }

    public List<Wort> alleWoerter() {
        return verzeichnis.values().stream().flatMap(List::stream).toList();
    }

    public List<Wort> findePartnerWoerter(Wort wort) {
        return verzeichnis.getOrDefault(Buchstabendifferenz.vonWort(wort), List.of());
    }
}
