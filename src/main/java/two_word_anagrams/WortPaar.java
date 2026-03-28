package two_word_anagrams;

public record WortPaar(Wort erstesWort, Wort zweitesWort) {

    @Override
    public String toString() {
        return erstesWort + " + " + zweitesWort;
    }
}
