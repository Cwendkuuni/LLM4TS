package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test cases for Metaphone class.
 */
public class MetaphoneTest {

    private Metaphone metaphone = new Metaphone();

    /**
     * Test metaphone encoding for basic English words.
     */
    @Test
    public void testBasicEncodings() {
        String[] words = new String[] {"apple", "banana", "cat", "dog", "elephant", "giraffe"};
        String[] expectedCodes = new String[] {"A0P0L0E0", "A0P1L1A1N1A1", "K1T0", "D1G0", "I1L1P1H1P1H1P1H1", "G1R1A1F1F1"};
        for (int i = 0; i < words.length; i++) {
            String code = metaphone.encode(words[i]);
            assertEquals(expectedCodes[i], code);
        }
    }

    /**
     * Test metaphone encoding for words starting with X.
     */
    @Test
    public void testXWordsEncoding() {
        String[] words = new String[] {"xylophone", "xenophobia", "xenon"};
        String[] expectedCodes = new String[] {"X0L1F1O1N1", "S1X1N1O1P1H1A1", "X1N0N0"};
        for (int i = 0; i < words.length; i++) {
            String code = metaphone.encode(words[i]);
            assertEquals(expectedCodes[i], code);
        }
    }

    /**
     * Test metaphone encoding for words with digits.
     */
    @Test
    public void testWordsWithDigitsEncoding() {
        String[] words = new String[] {"phone123", "325dog", "cat567"};
        String[] expectedCodes = new String[] {"P1H0N1E1", "D1G0", "K1T0"};
        for (int i = 0; i < words.length; i++) {
            String code = metaphone.encode(words[i]);
            assertEquals(expectedCodes[i], code);
        }
    }

    /**
     * Test metaphone encoding for punctuated words.
     */
    @Test
    public void testPunctuatedWordsEncoding() {
        String[] words = new String[] {"dog's name", "a, b, c"};
        String[] expectedCodes = new String[] {"D1G0S1", "A0B0C0"};
        for (int i = 0; i < words.length; i++) {
            String code = metaphone.encode(words[i]);
            assertEquals(expectedCodes[i], code);
        }
    }

    /**
     * Test metaphone encoding for empty string.
     */
    @Test(expected = EncoderException.class)
    public void testEmptyStringEncoding() {
        String code = metaphone.encode("");
        fail("Expected EncoderException");
    }

    /**
     * Test metaphone encoding for null string.
     */
    @Test(expected = EncoderException.class)
    public void testNullStringEncoding() {
        try {
            String code = metaphone.encode(null);
            fail("Expected EncoderException");
        } catch (NullPointerException ex) {
            // Expected, but JUnit doesn't support testing for specific exception types
            // Use @Test(expected = NullPointerException.class) instead in JUnit 4.12+
        }
    }

    /**
     * Test metaphone decoding.
     */
    @Test
    public void testDecoding() {
        String[] codes = new String[] {"A0P0L0E0", "S1X1N1O1P1H1A1", "X1N0N0", "P1H0N1E1", "S1X1N1O1P1H1P1H1P1H1", "G1R1A1F1F1"};
        String[] words = new String[] {"apple", "xenophobia", "xenon", "phone", "elephant", "giraffe"};
        for (int i = 0; i < codes.length; i++) {
            String word = metaphone.decode(codes[i]);
            assertEquals(words[i], word);
        }
    }

    /**
     * Test metaphone decoding for invalid code.
     */
    @Test(expected = EncoderException.class)
    public void testInvalidCodeDecoding() {
        String code = "A0P0L0E0X";
        try {
            String word = metaphone.decode(code);
            fail("Expected EncoderException");
        } catch (EncoderException ex) {
            // Expected
        }
    }
}