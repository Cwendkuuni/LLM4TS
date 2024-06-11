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
     * Test for {@link SoundexUtils#clean(String)}.
     */
    @Test
    public void testClean() {
        String input = "The quick Brown fox jumps over the lazy Dog";
        String expected = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
        String actual = SoundexUtils.clean(input);
        assertEquals(expected, actual);

        input = "12345";
        expected = "";
        actual = SoundexUtils.clean(input);
        assertEquals(expected, actual);

        input = null;
        expected = "";
        actual = SoundexUtils.clean(input);
        assertEquals(expected, actual);

        input = "";
        expected = "";
        actual = SoundexUtils.clean(input);
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link SoundexUtils#difference(StringEncoder, String, String)}.
     */
    @Test
    public void testDifferenceEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();

        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);

        s1 = "Apple";
        s2 = "Apple";
        expected = 4;
        actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);

        s1 = "Soundex";
        s2 = "Soundex";
        expected = 4;
        actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);

        s1 = "Apple";
        s2 = "Apple1";
        expected = 1;
        actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link SoundexUtils#differenceEncoded(String, String)}.
     */
    @Test
    public void testDifferenceEncoded() {
        String es1 = "A123";
        String es2 = "A234";
        int expected = 1;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);

        es1 = "A123";
        es2 = "A123";
        expected = 4;
        actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);

        es1 = "A123";
        es2 = "A321";
        expected = 2;
        actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }
}