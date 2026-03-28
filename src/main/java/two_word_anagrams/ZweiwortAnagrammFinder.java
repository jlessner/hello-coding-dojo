package two_word_anagrams;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ZweiwortAnagrammFinder {

    private static final Wort ZIELWORT = new Wort("documenting");

    public List<WortPaar> finde(Path vokabularDatei) throws IOException {
        // Schritt 1: Vokabular einlesen
        List<Wort> vokabular = new VokabularEinleser().liesVokabular(vokabularDatei);

        // Schritt 2: Differenzverzeichnis bilden
        Differenzverzeichnis differenzverzeichnis = new Differenzverzeichnis();
        differenzverzeichnis.erstelle(vokabular, ZIELWORT);

        // Schritt 3: Wortpaare ermitteln
        List<WortPaar> anagramme = new ArrayList<>();
        for (Wort aktuellesWort : differenzverzeichnis.alleWoerter()) {
            for (Wort partnerWort : differenzverzeichnis.findePartnerWoerter(aktuellesWort)) {
                anagramme.add(new WortPaar(aktuellesWort, partnerWort));
            }
        }

        return anagramme;
    }

    public static void main(String[] args) throws IOException {
        ZweiwortAnagrammFinder finder = new ZweiwortAnagrammFinder();
        List<WortPaar> anagramme = finder.finde(Path.of("wordlist.txt"));
        anagramme.forEach(System.out::println);
    }
}
