package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

    /**
     * Tests encoding of simple strings using default US_ENGLISH mapping.
     */
    @Test
    public void testSimpleEncoding() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple";
        String expected = "A120";
        String actual = soundex.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests encoding of strings with multiple consonants using default US_ENGLISH mapping.
     */
    @Test
    public void testMultipleConsonantsEncoding() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Banana";
        String expected = "B122";
        String actual = soundex.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests encoding of strings with multiple vowels using default US_ENGLISH mapping.
     */
    @Test
    public void testMultipleVowelsEncoding() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Elephant";
        String expected = "E111";
        String actual = soundex.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests encoding of strings with initial H or W using default US_ENGLISH mapping.
     */
    @Test
    public void testInitialHWEncoding() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Howard";
        String expected = "H222";
        String actual = soundex.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests encoding of strings with mixed H and W using default US_ENGLISH mapping.
     */
    @Test
    public void testMixedHWEncoding() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Whitaker";
        String expected = "W120";
        String actual = soundex.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests encoding of strings with digits using default US_ENGLISH mapping.
     */
    @Test
    public void testEncodingWithDigits() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple123";
        String expected = "A120";
        String actual = soundex.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests encoding of strings with spaces using default US_ENGLISH mapping.
     */
    @Test
    public void testEncodingWithSpaces() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple apple";
        String expected = "A120";
        String actual = soundex.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests encoding of strings with capital letters using default US_ENGLISH mapping.
     */
    @Test
    public void testEncodingWithCapitalLetters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple ApPlE";
        String expected = "A120";
        String actual = soundex.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests encoding of strings with non-English characters using custom mapping.
     */
    @Test
    public void testEncodingWithNonEnglishCharacters() throws EncoderException {
        Soundex soundex = new Soundex(Soundex.CzechMapping.CZECH_MAPPING);
        String input = "Město";
        String expected = "N110";
        String actual = soundex.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests encoding of empty string.
     */
    @Test
    public void testEncodingEmptyString() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "";
        String expected = "";
        String actual = soundex.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests encoding of null string.
     */
    @Test(expected = NullPointerException.class)
    public void testEncodingNullString() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = null;
        soundex.encode(input);
    }

    /**
     * Custom mapping for Czech language.
     */
    public static final char[] CzechMapping = new char[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};

    /**
     * Custom enumeration for Czech mapping.
     */
    public enum CzechMappingMapping {
        CZECH_MAPPING(CzechMapping)
    }
}