package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link SoundexUtils}.
 */
public class SoundexUtilsTest {

    /**
     * Test method for {@link SoundexUtils#clean(String)}.
     */
    @Test
    public void testClean() {
        // typical use case
        String input = "Hello World";
        String expected = "HELLO";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        // edge case: empty string
        String input2 = "";
        String result2 = SoundexUtils.clean(input2);
        assertEquals(input2, result2);

        // error case: null input
        String input3 = null;
        try {
            String result3 = SoundexUtils.clean(input3);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }
    }

    /**
     * Test method for {@link SoundexUtils#difference(StringEncoder, String, String)}.
     */
    @Test
    public void testDifference() throws EncoderException {
        // typical use case
        String encoderType = "Soundex";
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(new SoundexEncoder(), s1, s2);
        assertEquals(expected, result);

        // edge case: identical strings
        String s3 = "Apple";
        String s4 = "Apple";
        int expected2 = 4;
        int result2 = SoundexUtils.difference(new SoundexEncoder(), s3, s4);
        assertEquals(expected2, result2);

        // error case: null input
        String s5 = null;
        try {
            int result3 = SoundexUtils.difference(new SoundexEncoder(), s5, s1);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }

        // error case: null encoder
        StringEncoder encoder = null;
        try {
            int result4 = SoundexUtils.difference(encoder, s1, s2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Test method for {@link SoundexUtils#differenceEncoded(String, String)}.
     */
    @Test
    public void testDifferenceEncoded() {
        // typical use case
        String es1 = "123";
        String es2 = "321";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        // edge case: identical encoded strings
        String es3 = "111";
        String es4 = "111";
        int expected2 = 4;
        int result2 = SoundexUtils.differenceEncoded(es3, es4);
        assertEquals(expected2, result2);

        // error case: null input
        String es5 = null;
        try {
            int result3 = SoundexUtils.differenceEncoded(es1, es5);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }
}