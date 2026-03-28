package two_word_anagrams;

import java.util.Optional;

public class LetterCounter {

    private static final int ALPHABET_SIZE = 26;

    public int[] count(String word) {
        int[] counts = new int[ALPHABET_SIZE];
        for (char c : word.toLowerCase().toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                counts[c - 'a']++;
            }
        }
        return counts;
    }

    public Optional<int[]> subtract(int[] from, int[] letters) {
        int[] result = from.clone();
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            result[i] -= letters[i];
            if (result[i] < 0) {
                return Optional.empty();
            }
        }
        return Optional.of(result);
    }

    public String toSignature(int[] counts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (counts[i] > 0) {
                sb.append((char) ('a' + i));
                sb.append(counts[i]);
            }
        }
        return sb.toString();
    }
}