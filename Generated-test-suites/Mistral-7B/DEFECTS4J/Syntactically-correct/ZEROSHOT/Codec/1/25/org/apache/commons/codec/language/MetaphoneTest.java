package org.apache.commons.codec.language;

import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;

/**
 * Test file for Metaphone class
 */
public class MetaphoneTest {

    /**
     * Create a new instance of Metaphone encoder.
     */
    private Metaphone metaphone = new Metaphone();

    /**
     * Test metaphone encoding of single character strings.
     */
    @Test
    public void testSingleCharString() {
        // Testing for empty string
        assertEquals("", metaphone.metaphone(""));

        // Testing for single character strings
        for (char c = 'A'; c <= 'Z'; c++) {
            String expected = String.valueOf(c).toUpperCase();
            String actual = metaphone.metaphone(String.valueOf(c));
            assertEquals(expected, actual);
        }
    }

    /**
     * Test metaphone encoding of two character strings: KN, AA, WH, WR, AE, etc.
     */
    @Test
    public void testTwoCharStrings() {
        // Testing for KN
        assertEquals("KN", metaphone.metaphone("KN"));
        // Testing for AA
        assertEquals("AA", metaphone.metaphone("AA"));
        // Testing for WH
        assertEquals("W0", metaphone.metaphone("WH"));
        // Testing for WR
        assertEquals("WR", metaphone.metaphone("WR"));
        // Testing for AE
        assertEquals("AE", metaphone.metaphone("AE"));
    }

    /**
     * Test metaphone encoding of multiple character strings.
     */
    @Test
    public void testMultipleCharStrings() {
        // Testing for simple strings
        assertEquals("BA", metaphone.metaphone("Banana"));
        assertEquals("BE", metaphone.metaphone("Apple"));
        assertEquals("BH", metaphone.metaphone("Banana"));
        assertEquals("BT", metaphone.metaphone("Apple"));
        assertEquals("BQ", metaphone.metaphone("Bottle"));
        assertEquals("BK", metaphone.metaphone("Bottle"));

        // Testing for strings with consonant clusters
        assertEquals("CP", metaphone.metaphone("Cipactli"));
        assertEquals("CS", metaphone.metaphone("Cipsas"));
        assertEquals("CK", metaphone.metaphone("Cipsas"));

        // Testing for strings with silent "h" at the end
        assertEquals("KJ", metaphone.metaphone("Bughouse"));
        assertEquals("KJ", metaphone.metaphone("Buggy"));
        assertEquals("KJ", metaphone.metaphone("Bugger"));

        // Testing for strings with silent "g" at the end
        assertEquals("JN", metaphone.metaphone("Dogfish"));
        assertEquals("JN", metaphone.metaphone("Dogfish"));
        assertEquals("JN", metaphone.metaphone("Dogfood"));

        // Testing for strings with double consonants
        assertEquals("KK", metaphone.metaphone("Bookkeeper"));
        assertEquals("KK", metaphone.metaphone("Coochie-coo"));

        // Testing for strings with multiple vowels
        assertEquals("AE", metaphone.metaphone("Aardvark"));
        assertEquals("AE", metaphone.metaphone("Banana"));
        assertEquals("AE", metaphone.metaphone("Elephant"));

        // Testing for strings with "Ch" and "Th"
        assertEquals("XS", metaphone.metaphone("Bathysphere"));
        assertEquals("XS", metaphone.metaphone("Bathysphere"));
        assertEquals("TH", metaphone.metaphone("Bathytherm"));
        assertEquals("TH", metaphone.metaphone("Bathytherm"));

        // Testing for strings with "sh", "si", "sio", and "sia"
        assertEquals("SX", metaphone.metaphone("Pashalim"));
        assertEquals("SX", metaphone.metaphone("Pashalim"));
        assertEquals("SX", metaphone.metaphone("Pashalim"));
        assertEquals("SX", metaphone.metaphone("Pashalim"));
    }

    /**
     * Test metaphone encoding of strings with special characters and numbers.
     */
    @Test
    public void testSpecialCharactersAndNumbers() {
        // Testing for strings with special characters
        assertEquals("BH", metaphone.metaphone("Banana!"));
        assertEquals("BH", metaphone.metaphone("Banana!"));

        // Testing for strings with numbers
        assertEquals("BH", metaphone.metaphone("Banana123"));
        assertEquals("BH", metaphone.metaphone("Banana123"));

        // Testing for strings with numbers and special characters
        assertEquals("BH", metaphone.metaphone("Banana!123"));
        assertEquals("BH", metaphone.metaphone("Banana!123"));
    }

    /**
     * Test metaphone encoding of strings with vowel combinations
     */
    @Test
    public void testVowelCombinations() {
        // Testing for strings with "ai" combination
        assertEquals("AE", metaphone.metaphone("Anais"));
        assertEquals("AE", metaphone.metaphone("Anais"));

        // Testing for strings with "ei" combination
        assertEquals("EY", metaphone.metaphone("Mei"));
        assertEquals("EY", metaphone.metaphone("Mei"));

        // Testing for strings with "oi" combination
        assertEquals("OY", metaphone.metaphone("Noise"));
        assertEquals("OY", metaphone.metaphone("Noise"));

        // Testing for strings with "ou" combination
        assertEquals("OU", metaphone.metaphone("Boulder"));
        assertEquals("OU", metaphone.metaphone("Boulder"));

        // Testing for strings with "au" combination
        assertEquals("AW", metaphone.metaphone("Spaghetti"));
        assertEquals("AW", metaphone.metaphone("Spaghetti"));

        // Testing for strings with "ou" and "au" combinations
        assertEquals("OU", metaphone.metaphone("Douglas"));
        assertEquals("AW", metaphone.metaphone("Spaghettiou"));
    }

    /**
     * Testing if metaphone encoding returns empty string for null values.
     */
    @Test(expected = NullPointerException.class)
    public void testNullString() {
        metaphone.metaphone(null);
    }
}