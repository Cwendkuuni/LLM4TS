package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Typical use case
        String input = "Hello World";
        String expected = "HELLO";
        assertEquals(expected, SoundexUtils.clean(input));

        // Edge case: empty string
        String empty = "";
        assertEquals(empty, SoundexUtils.clean(empty));

        // Error case: null input
        String nullInput = null;
        assertNull(SoundexUtils.clean(nullInput));

        // Error case: non-letter characters
        String inputWithNumbers = "Hello123";
        String expectedWithNumbers = "HELLO";
        assertEquals(expectedWithNumbers, SoundexUtils.clean(inputWithNumbers));

        // Edge case: long input
        String longInput = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
        String expectedLongInput = "LOREM IPsum DOLOr SIT Amet";
        assertEquals(expectedLongInput, SoundexUtils.clean(longInput));
    }

    @Test
    public void testDifference() {
        // Typical use case
        String encoderType = "Soundex";
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        try {
            int actual = SoundexUtils.difference(new SoundexEncoder(), s1, s2);
            assertEquals(expected, actual);
        } catch (EncoderException e) {
            fail("Error encoding strings: " + e.getMessage());
        }

        // Edge case: identical strings
        String s1Identical = "Apple";
        String s2Identical = "Apple";
        int expectedIdentical = 4;
        try {
            int actualIdentical = SoundexUtils.difference(new SoundexEncoder(), s1Identical, s2Identical);
            assertEquals(expectedIdentical, actualIdentical);
        } catch (EncoderException e) {
            fail("Error encoding strings: " + e.getMessage());
        }

        // Error case: null input
        String s1Null = null;
        String s2 = "Orange";
        try {
            int actualNull = SoundexUtils.difference(new SoundexEncoder(), s1Null, s2);
            fail("Expected an EncoderException but got: " + actualNull);
        } catch (EncoderException e) {
            // Expected
        }

        // Error case: encoder not initialized
        SoundexEncoder encoder = null;
        String s1Error = "Apple";
        String s2Error = "Orange";
        try {
            int actualError = SoundexUtils.difference(encoder, s1Error, s2Error);
            fail("Expected an EncoderException but got: " + actualError);
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testDifferenceEncoded() {
        // Typical use case
        String encodedString1 = "tnit";
        String encodedString2 = "tns";
        int expected = 3;
        int actual = SoundexUtils.differenceEncoded(encodedString1, encodedString2);
        assertEquals(expected, actual);

        // Edge case: identical encoded strings
        String encodedString1Identical = "tnit";
        String encodedString2Identical = "tnit";
        int expectedIdentical = 4;
        int actualIdentical = SoundexUtils.differenceEncoded(encodedString1Identical, encodedString2Identical);
        assertEquals(expectedIdentical, actualIdentical);

        // Error case: null input
        String encodedString1Null = null;
        String encodedString2 = "tns";
        int expectedNull = 0;
        int actualNull = SoundexUtils.differenceEncoded(encodedString1Null, encodedString2);
        assertEquals(expectedNull, actualNull);

        // Error case: encoded strings of different lengths
        String encodedString1DifferentLength = "tnit";
        String encodedString2DifferentLength = "tnsa";
        int expectedDifferentLength = 0;
        int actualDifferentLength = SoundexUtils.differenceEncoded(encodedString1DifferentLength, encodedString2DifferentLength);
        assertEquals(expectedDifferentLength, actualDifferentLength);
    }
}