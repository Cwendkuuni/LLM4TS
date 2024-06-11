package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the SoundexUtils class.
 */
public class SoundexUtilsTest {

    /**
     * Test for the clean method with null input.
     */
    @Test
    public void testCleanNullInput() {
        String input = null;
        String expected = null;
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for the clean method with empty input.
     */
    @Test
    public void testCleanEmptyInput() {
        String input = "";
        String expected = "";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for the clean method with single letter input.
     */
    @Test
    public void testCleanSingleLetterInput() {
        String input = "A";
        String expected = "A";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for the clean method with multiple letters input.
     */
    @Test
    public void testCleanMultipleLettersInput() {
        String input = "Apple";
        String expected = "APPL";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for the clean method with digits input.
     */
    @Test
    public void testCleanDigitsInput() {
        String input = "12345";
        String expected = "";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for the clean method with special characters input.
     */
    @Test
    public void testCleanSpecialCharactersInput() {
        String input = "Apple!";
        String expected = "APPL";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for the difference method with null encoder.
     */
    @Test(expected = NullPointerException.class)
    public void testDifferenceNullEncoder() {
        String encoderName = null;
        String s1 = "Apple";
        String s2 = "Banana";
        SoundexUtils.difference(null, s1, s2);
    }

    /**
     * Test for the difference method with null input String 1.
     */
    @Test(expected = NullPointerException.class)
    public void testDifferenceNullInputString1() {
        String encoderName = "Soundex";
        String s1 = null;
        String s2 = "Banana";
        SoundexUtils.difference(encoderName, s1, s2);
    }

    /**
     * Test for the difference method with null input String 2.
     */
    @Test(expected = NullPointerException.class)
    public void testDifferenceNullInputString2() {
        String encoderName = "Soundex";
        String s1 = "Apple";
        String s2 = null;
        SoundexUtils.difference(encoderName, s1, s2);
    }

    /**
     * Test for the difference method with empty input Strings.
     */
    @Test
    public void testDifferenceEmptyInputStrings() {
        String encoderName = "Soundex";
        String s1 = "";
        String s2 = "";
        int expected = 4;
        int result = SoundexUtils.difference(encoderName, s1, s2);
        assertEquals(expected, result);
    }

    /**
     * Test for the difference method with same input String.
     */
    @Test
    public void testDifferenceSameInputStrings() {
        String encoderName = "Soundex";
        String s1 = "Apple";
        String s2 = "Apple";
        int expected = 4;
        int result = SoundexUtils.difference(encoderName, s1, s2);
        assertEquals(expected, result);
    }

    /**
     * Test for the difference method with different input Strings.
     */
    @Test
    public void testDifferenceDifferentInputStrings() {
        String encoderName = "Soundex";
        String s1 = "Apple";
        String s2 = "Banana";
        int expected = 0;
        int result = SoundexUtils.difference(encoderName, s1, s2);
        assertEquals(expected, result);
    }

    /**
     * Test for the difference method with encoding error.
     */
    @Test(expected = EncoderException.class)
    public void testDifferenceEncodingError() {
        String encoderName = "InvalidEncoder";
        String s1 = "Apple";
        String s2 = "Banana";
        SoundexUtils.difference(encoderName, s1, s2);
    }

    /**
     * Test for the differenceEncoded method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testDifferenceEncodedNullInput() {
        String es1 = null;
        String es2 = "Banana";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    /**
     * Test for the differenceEncoded method with empty input.
     */
    @Test
    public void testDifferenceEncodedEmptyInput() {
        String es1 = "";
        String es2 = "";
        int expected = 4;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    /**
     * Test for the differenceEncoded method with same input.
     */
    @Test
    public void testDifferenceEncodedSameInput() {
        String es1 = "1234512345";
        String es2 = "1234512345";
        int expected = 4;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    /**
     * Test for the differenceEncoded method with different input.
     */
    @Test
    public void testDifferenceEncodedDifferentInput() {
        String es1 = "1234512345";
        String es2 = "6789067890";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }
}