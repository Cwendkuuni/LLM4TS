package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link Metaphone} class.
 */
public class MetaphoneTest {

    private final Metaphone metaphone = new Metaphone();

    /**
     * Test metaphone encoding for simple words.
     */
    @Test
    public void testSimpleWords() {
        String[] words = {"apple", "banana", "cat", "dog", "elephant", "hello", "india", "kangaroo", "one", "potato", "quicksand", "tomato"};
        String[] codes = {"AA013", "A0010", "K3213", "D1611", "E1111", "HH000", "I1311", "K13031", "W111", "P3211", "Q0001X", "T2320"};
        for (int i = 0; i < words.length; i++) {
            String code = metaphone.metaphone(words[i]);
            Assert.assertEquals(codes[i], code);
        }
    }

    /**
     * Test metaphone encoding for complex words.
     */
    @Test
    public void testComplexWords() {
        String[] words = {"metaphone", "algorithm", "encapsulation", "programming", "development", "computational", "complexity", "functionality", "implementation", "initialization", "instantiation", "encapsulated", "functional", "initializing", "instantiating"};
        String[] codes = {"M33310P1H3N1E0", "A0011G0001L01G00M0", "E1110C21P1G00N1G00", "P2311R0001G01M00", "D0111E001V11P0G0", "K2222C001P00M00L00", "S1311P1311G01G01", "F1111N001G00L00T0", "I3211M001P1G1M00", "I1311N1211Z00N1G00", "E1111C111P1G1N1G1", "F1111N001G00L00F0", "I1311N1211Z00N1G1I0", "I1311N1211Z00N1G1N0"};
        for (int i = 0; i < words.length; i++) {
            String code = metaphone.metaphone(words[i]);
            Assert.assertEquals(codes[i], code);
        }
    }

    /**
     * Test metaphone encoding for empty string.
     */
    @Test
    public void testEmptyString() {
        String code = metaphone.metaphone("");
        Assert.assertEquals("", code);
    }

    /**
     * Test metaphone encoding for null string.
     */
    @Test(expected = EncoderException.class)
    public void testNullString() {
        metaphone.metaphone(null);
    }

    /**
     * Test metaphone encoding for long string.
     */
    @Test
    public void testLongString() {
        String longString = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";
        String code = metaphone.metaphone(longString);
        Assert.assertNotNull(code);
    }

    /**
     * Test metaphone encoding for special characters.
     */
    @Test
    public void testSpecialCharacters() {
        String[] words = {"apple!", "banan#a", "cat$", "dog%", "elephant!", "hello_world", "india@", "kangaroo$", "one%", "potato!", "quicksand&", "tomato^"};
        String[] codes = {"AA013!", "A0010#A", "K3213$", "D1611%", "E1111!", "HH000_W0R1", "I1311@", "K13031$", "W111%", "P3211!", "Q0001X&", "T2320^"};
        for (int i = 0; i < words.length; i++) {
            String code = metaphone.metaphone(words[i]);
            Assert.assertEquals(codes[i], code);
        }
    }

    /**
     * Test metaphone encoding for empty array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArray() {
        metaphone.metaphone(new String[0]);
    }

    /**
     * Test metaphone encoding for null array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullArray() {
        metaphone.metaphone(null);
    }

    /**
     * Test metaphone encoding for long array.
     */
    @Test
    public void testLongArray() {
        String[] words = new String[1000];
        for (int i = 0; i < words.length; i++) {
            words[i] = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";
        }
        String[] codes = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            String code = metaphone.metaphone(words[i]);
            codes[i] = code;
        }
        for (int i = 0; i < codes.length; i++) {
            Assert.assertNotNull(codes[i]);
        }
    }
}