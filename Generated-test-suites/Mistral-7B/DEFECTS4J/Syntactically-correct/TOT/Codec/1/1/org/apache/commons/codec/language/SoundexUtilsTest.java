package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link SoundexUtils} class.
 */
public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // typical use case
        String input = "Apache Commons Codec";
        String expected = "Apache Commons Codec";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        // edge case: empty string
        String empty = "";
        String emptyClean = "";
        result = SoundexUtils.clean(empty);
        assertEquals(emptyClean, result);

        // error scenario: null input
        String nullInput = null;
        String nullClean = null;
        try {
            result = SoundexUtils.clean(nullInput);
        } catch (NullPointerException e) {
            nullClean = null;
        }
        assertEquals(nullClean, result);
    }

    @Test
    public void testDifference() throws EncoderException {
        // typical use case
        String encoderType = "Soundex";
        String str1 = "Apple";
        String str2 = "Apple";
        int expected = 4;
        int result = SoundexUtils.difference(new SoundexEncoder(), str1, str2);
        assertEquals(expected, result);

        // edge case: different encoder types
        encoderType = "RefinedSoundex";
        String str3 = "Apple";
        String str4 = "Orange";
        expected = 0;
        result = SoundexUtils.difference(new RefinedSoundexEncoder(), str3, str4);
        assertEquals(expected, result);

        // error scenario: null input for one of the strings
        String str5 = null;
        try {
            result = SoundexUtils.difference(new SoundexEncoder(), str1, str5);
        } catch (NullPointerException e) {
            result = -1;
        }
        assertEquals(-1, result);
    }
}