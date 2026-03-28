package two_word_anagrams;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Buchstabendifferenz {

    private final String schluessel;

    private Buchstabendifferenz(String schluessel) {
        this.schluessel = schluessel;
    }

    public static Buchstabendifferenz vonWortGegenZielwort(Wort wort, Wort zielwort) {
        Map<Character, Integer> differenz = new TreeMap<>(zaehleBuchstaben(zielwort));
        for (Map.Entry<Character, Integer> eintrag : zaehleBuchstaben(wort).entrySet()) {
            differenz.merge(eintrag.getKey(), -eintrag.getValue(), Integer::sum);
        }
        return new Buchstabendifferenz(alsSchluessel(differenz));
    }

    public static Buchstabendifferenz vonWort(Wort wort) {
        return new Buchstabendifferenz(alsSchluessel(zaehleBuchstaben(wort)));
    }

    public static boolean istGeeignet(Wort wort, Wort zielwort) {
        if (wort.text().length() >= zielwort.text().length()) return false;
        Map<Character, Integer> zielBuchstaben = zaehleBuchstaben(zielwort);
        for (Map.Entry<Character, Integer> eintrag : zaehleBuchstaben(wort).entrySet()) {
            if (!zielBuchstaben.containsKey(eintrag.getKey())) return false;
            if (eintrag.getValue() > zielBuchstaben.get(eintrag.getKey())) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Buchstabendifferenz other)) return false;
        return Objects.equals(schluessel, other.schluessel);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(schluessel);
    }

    private static Map<Character, Integer> zaehleBuchstaben(Wort wort) {
        Map<Character, Integer> haeufigkeiten = new TreeMap<>();
        for (char c : wort.text().toLowerCase().toCharArray()) {
            haeufigkeiten.merge(c, 1, Integer::sum);
        }
        return haeufigkeiten;
    }

    private static String alsSchluessel(Map<Character, Integer> buchstaben) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Character, Integer> eintrag : buchstaben.entrySet()) {
            sb.append(String.valueOf(eintrag.getKey()).repeat(eintrag.getValue()));
        }
        return sb.toString();
    }
}
