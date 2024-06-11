package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for the Soundex class.
 */
public class SoundexTest {

    /**
     * Test encoding of simple strings using default US_ENGLISH mapping.
     */
    @Test
    public void testEncodingWithDefaultMapping() {
        Soundex soundex = Soundex.US_ENGLISH;
        assertEquals("B021", soundex.encode("Bob"));
        assertEquals("B312", soundex.encode("Robert"));
        assertEquals("B130", soundex.encode("Bobby"));
        assertEquals("B300", soundex.encode("Rob"));
        assertEquals("B000", soundex.encode(""));
        assertEquals("B041", soundex.encode("Able"));
        assertEquals("B142", soundex.encode("William"));
        assertEquals("B250", soundex.encode("Samuel"));
    }

    /**
     * Test encoding of strings with multiple consonants in a row using default US_ENGLISH mapping.
     */
    @Test
    public void testEncodingWithMultipleConsonants() {
        Soundex soundex = Soundex.US_ENGLISH;
        assertEquals("B421", soundex.encode("Apple"));
        assertEquals("B311", soundex.encode("Butter"));
        assertEquals("B330", soundex.encode("Elephant"));
        assertEquals("B341", soundex.encode("Raspberry"));
        assertEquals("B320", soundex.encode("Coffee"));
    }

    /**
     * Test encoding of strings with complex consonant combinations using default US_ENGLISH mapping.
     */
    @Test
    public void testEncodingWithComplexConsonantCombinations() {
        Soundex soundex = Soundex.US_ENGLISH;
        assertEquals("B523", soundex.encode("Indiana"));
        assertEquals("B163", soundex.encode("Potato"));
        assertEquals("B360", soundex.encode("Quartet"));
        assertEquals("B152", soundex.encode("Cemetery"));
        assertEquals("B262", soundex.encode("Explosive"));
    }

    /**
     * Test encoding of strings with special characters using default US_ENGLISH mapping.
     */
    @Test
    public void testEncodingWithSpecialCharacters() {
        Soundex soundex = Soundex.US_ENGLISH;
        assertEquals("B000", soundex.encode(""));
        assertEquals("B142", soundex.encode("Williams"));
        assertEquals("B250", soundex.encode("Samuels"));
        assertEquals("B241", soundex.encode("Christmas"));
        assertEquals("B222", soundex.encode("Andrews"));
    }

    /**
     * Test encoding of strings using a custom mapping.
     */
    @Test
    public void testEncodingWithCustomMapping() {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'S', 'T', 'N', 'D'};
        Soundex soundex = new Soundex(customMapping);
        assertEquals("S012", soundex.encode("Apple"));
        assertEquals("T311", soundex.encode("Butter"));
        assertEquals("N330", soundex.encode("Elephant"));
        assertEquals("R341", soundex.encode("Raspberry"));
        assertEquals("C320", soundex.encode("Coffee"));
    }

    /**
     * Test encoding of an empty string with a custom mapping.
     */
    @Test
    public void testEncodingEmptyStringWithCustomMapping() {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'S', 'T', 'N', 'D'};
        Soundex soundex = new Soundex(customMapping);
        assertEquals("", soundex.encode(""));
    }

    /**
     * Test encoding of strings with error cases.
     */
    @Test(expected = EncoderException.class)
    public void testEncodingErrorCases() {
        Soundex soundex = Soundex.US_ENGLISH;
        soundex.encode(null);
        soundex.encode("123");
    }
}