package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link SoundexUtils} class.
 *
 * @since 1.0
 */
public class SoundexUtilsTest {

    /**
     * Test for {@link SoundexUtils#clean(String)} method.
     */
    @Test
    public void testClean() {
        String input = "The quick Brown fox jumps over the lazy dog";
        String expected = "The quick Brown Fox jumps over the lazy Dog";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "";
        expected = "";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = null;
        try {
            result = SoundexUtils.clean(input);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test for {@link SoundexUtils#difference(StringEncoder, String, String)} method.
     */
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        encoder = new org.apache.commons.codec.language.Metaphone();
        s1 = "Banana";
        s2 = "Apple";
        expected = 0;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = null;
        String errorMessage = "Expected NullPointerException";
        try {
            result = SoundexUtils.difference(encoder, s1, s2);
            fail(errorMessage);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test for {@link SoundexUtils#differenceEncoded(String, String)} method.
     */
    @Test
    public void testDifferenceEncoded() {
        String es1 = "122132";
        String es2 = "122232";
        int expected = 2;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = null;
        errorMessage = "Expected NullPointerException";
        try {
            result = SoundexUtils.differenceEncoded(es1, es2);
            fail(errorMessage);
        } catch (NullPointerException e) {
            // Expected
        }
    }
}