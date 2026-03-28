package two_word_anagrams;

public record Wort(String text) {

    @Override
    public String toString() {
        return text;
    }
}
