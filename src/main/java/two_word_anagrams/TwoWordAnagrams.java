package two_word_anagrams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwoWordAnagrams {

    public static void main(String[] args) throws IOException {
        List<String> words = readWords(Path.of("wordlist.txt"));
        List<String[]> results = findTwoWordAnagrams("documenting", words);
        results.forEach(pair -> System.out.println(pair[0] + " " + pair[1]));
    }

    static List<String> readWords(Path path) throws IOException {
        String content = Files.readString(path);
        return Arrays.stream(content.trim().split("\\s+"))
                .map(String::toLowerCase)
                .toList();
    }

    static List<String[]> findTwoWordAnagrams(String target, List<String> words) {
        char[] targetSorted = sortedChars(target);
        List<String[]> results = new ArrayList<>();

        for (int i = 0; i < words.size(); i++) {
            for (int j = i + 1; j < words.size(); j++) {
                String combined = words.get(i) + words.get(j);
                if (combined.length() == target.length()
                        && Arrays.equals(sortedChars(combined), targetSorted)) {
                    results.add(new String[]{words.get(i), words.get(j)});
                }
            }
        }
        return results;
    }

    private static char[] sortedChars(String s) {
        char[] chars = s.toLowerCase().toCharArray();
        Arrays.sort(chars);
        return chars;
    }
}
