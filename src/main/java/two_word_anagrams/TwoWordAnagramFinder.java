package two_word_anagrams;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class TwoWordAnagramFinder {

    private static final Logger LOGGER = Logger.getLogger(TwoWordAnagramFinder.class.getName());

    private final LetterCounter letterCounter = new LetterCounter();

    public List<WordPair> find(String target, List<String> words) {
        int[] targetCounts = letterCounter.count(target);
        Map<String, List<String>> signatureToWords = buildSignatureMap(words);

        List<WordPair> results = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (String word : words) {
            findComplementPairs(word, targetCounts, signatureToWords, seen, results);
        }
        return results;
    }

    private void findComplementPairs(String word, int[] targetCounts,
                                     Map<String, List<String>> signatureToWords,
                                     Set<String> seen, List<WordPair> results) {
        int[] wordCounts = letterCounter.count(word);
        letterCounter.subtract(targetCounts, wordCounts).ifPresent(remaining -> {
            String complementSig = letterCounter.toSignature(remaining);
            List<String> complements = signatureToWords.getOrDefault(complementSig, List.of());
            for (String complement : complements) {
                if (complement.equals(word)) {
                    continue;
                }
                addPairIfNew(word, complement, seen, results);
            }
        });
    }

    private void addPairIfNew(String word, String complement, Set<String> seen, List<WordPair> results) {
        String first = word.compareTo(complement) <= 0 ? word : complement;
        String second = word.compareTo(complement) <= 0 ? complement : word;
        String key = first + "|" + second;
        if (seen.add(key)) {
            results.add(new WordPair(first, second));
        }
    }

    private Map<String, List<String>> buildSignatureMap(List<String> words) {
        Map<String, List<String>> map = new HashMap<>();
        for (String word : words) {
            String sig = letterCounter.toSignature(letterCounter.count(word));
            map.computeIfAbsent(sig, k -> new ArrayList<>()).add(word);
        }
        return map;
    }

    public static void main(String[] args) throws IOException {
        WordlistReader reader = new WordlistReader();
        List<String> words = reader.read(Path.of("wordlist.txt"));

        TwoWordAnagramFinder finder = new TwoWordAnagramFinder();
        List<WordPair> results = finder.find("documenting", words);

        if (results.isEmpty()) {
            LOGGER.info("Keine Zwei-Wort-Anagramme gefunden.");
        } else {
            LOGGER.info("Zwei-Wort-Anagramme von \"documenting\":");
            for (WordPair pair : results) {
                LOGGER.info(() -> "  " + pair.first() + " + " + pair.second());
            }
        }
    }
}
