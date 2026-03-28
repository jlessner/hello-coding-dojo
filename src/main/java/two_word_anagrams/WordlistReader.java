package two_word_anagrams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class WordlistReader {

    public List<String> read(Path path) throws IOException {
        String content = Files.readString(path);
        return Arrays.stream(content.split("\\s+"))
                .filter(w -> !w.isBlank())
                .map(String::toLowerCase)
                .toList();
    }
}
