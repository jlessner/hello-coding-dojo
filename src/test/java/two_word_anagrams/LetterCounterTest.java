package two_word_anagrams;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LetterCounterTest {

    private final LetterCounter counter = new LetterCounter();

    @Test
    void countsLetters() {
        int[] counts = counter.count("aab");
        assertEquals(2, counts[0]); // 'a'
        assertEquals(1, counts[1]); // 'b'
    }

    @Test
    void countIsCaseInsensitive() {
        int[] lower = counter.count("abc");
        int[] upper = counter.count("ABC");
        assertArrayEquals(lower, upper);
    }

    @Test
    void subtractSucceeds() {
        int[] target = counter.count("documenting");
        int[] word = counter.count("coding");

        Optional<int[]> remaining = counter.subtract(target, word);

        assertTrue(remaining.isPresent());
        // "documenting" - "coding" leaves "ument"
        assertArrayEquals(counter.count("ument"), remaining.get());
    }

    @Test
    void subtractReturnsEmptyWhenImpossible() {
        int[] target = counter.count("abc");
        int[] word = counter.count("abcd");

        assertTrue(counter.subtract(target, word).isEmpty());
    }

    @Test
    void toSignatureIsConsistentForAnagrams() {
        String sig1 = counter.toSignature(counter.count("listen"));
        String sig2 = counter.toSignature(counter.count("silent"));
        assertEquals(sig1, sig2);
    }

    @Test
    void toSignatureIsDifferentForNonAnagrams() {
        String sig1 = counter.toSignature(counter.count("hello"));
        String sig2 = counter.toSignature(counter.count("world"));
        assertNotEquals(sig1, sig2);
    }
}
