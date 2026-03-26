package anagramms_claude_opus_4_5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Findet Zwei-Wort-Anagramme für einen gegebenen String.
 * Ein Zwei-Wort-Anagramm besteht aus zwei Wörtern, die zusammen
 * ein Anagramm des Zielworts bilden.
 */
public class TwoWordAnagramFinder {

    private final Set<String> wordList;

    public TwoWordAnagramFinder(Set<String> wordList) {
        this.wordList = normalizeWordList(wordList);
    }

    public static TwoWordAnagramFinder fromFile(Path filePath) throws IOException {
        try (var lines = Files.lines(filePath)) {
            Set<String> words = lines
                    .flatMap(line -> Arrays.stream(line.split("\\s+")))
                    .filter(word -> !word.isBlank())
                    .collect(Collectors.toSet());
            return new TwoWordAnagramFinder(words);
        }
    }

    public List<WordPair> findTwoWordAnagrams(String target) {
        String normalizedTarget = normalize(target);
        char[] targetChars = sorted(normalizedTarget);

        return wordList.stream()
                .filter(firstWord -> firstWord.length() < normalizedTarget.length())
                .flatMap(firstWord -> findMatchingSecondWords(firstWord, targetChars, normalizedTarget.length()).stream()
                        .map(secondWord -> new WordPair(firstWord, secondWord)))
                .distinct()
                .sorted()
                .toList();
    }

    private List<String> findMatchingSecondWords(String firstWord, char[] targetChars, int targetLength) {
        int requiredLength = targetLength - firstWord.length();
        char[] firstWordChars = sorted(firstWord);
        char[] remainingChars = subtractChars(targetChars, firstWordChars);

        if (remainingChars == null) {
            return List.of();
        }

        return wordList.stream()
                .filter(secondWord -> secondWord.length() == requiredLength)
                .filter(secondWord -> Arrays.equals(sorted(secondWord), remainingChars))
                .filter(secondWord -> firstWord.compareTo(secondWord) < 0)
                .toList();
    }

    private char[] subtractChars(char[] source, char[] toSubtract) {
        char[] result = source.clone();
        for (char c : toSubtract) {
            int index = findChar(result, c);
            if (index < 0) {
                return null;
            }
            result[index] = '\0';
        }
        return Arrays.stream(new String(result).split(""))
                .filter(s -> !s.equals("\0"))
                .collect(Collectors.joining())
                .toCharArray();
    }

    private int findChar(char[] chars, char c) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) {
                return i;
            }
        }
        return -1;
    }

    private char[] sorted(String s) {
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return chars;
    }

    private String normalize(String s) {
        return s.toLowerCase().replaceAll("\\s+", "");
    }

    private Set<String> normalizeWordList(Set<String> words) {
        return words.stream()
                .map(String::toLowerCase)
                .map(String::trim)
                .filter(word -> !word.isBlank())
                .collect(Collectors.toSet());
    }

    public record WordPair(String first, String second) implements Comparable<WordPair> {
        @Override
        public int compareTo(WordPair other) {
            int cmp = first.compareTo(other.first);
            return cmp != 0 ? cmp : second.compareTo(other.second);
        }

        @Override
        public String toString() {
            return first + " " + second;
        }
    }
}

