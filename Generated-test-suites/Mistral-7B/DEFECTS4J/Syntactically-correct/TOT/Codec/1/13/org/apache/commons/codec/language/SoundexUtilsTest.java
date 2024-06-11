package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the SoundexUtils class.
 */
public class SoundexUtilsTest {

    /**
     * Test case for the clean method.
     */
    @Test
    public void testClean() {
        String input = "The quick Brown Fox jumps over the lazy Dog";
        String expected = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "1A";
        expected = "1A";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = null;
        result = SoundexUtils.clean(input);
        assertNull(result);
    }

    /**
     * Test case for the difference method with Soundex encoder.
     */
    @Test
    public void testDifferenceSoundex() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.soundex.Soundex();
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

        s1 = "Apple";
        s2 = "Apple1";
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(0, result);

        s1 = null;
        String s3 = "Apple";
        result = SoundexUtils.difference(encoder, s1, s3);
        assertNull(result);
    }

    /**
     * Test case for the differenceEncoded method with Soundex encoder.
     */
    @Test
    public void testDifferenceEncodedSoundex() {
        String es1 = "A123";
        String es2 = "B456";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "A123";
        es2 = "A123";
        expected = 4;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "A123";
        es2 = "A124";
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, result);

        es1 = null;
        String es3 = "A123";
        result = SoundexUtils.differenceEncoded(es1, es3);
        assertNull(result);
    }

    /**
     * Test case for the difference method with RefinedSoundex encoder.
     */
    @Test
    public void testDifferenceRefinedSoundex() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.refinedsoundex.RefinedSoundex();
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

        s1 = "Apple";
        s2 = "Apple1";
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(0, result);

        s1 = null;
        String s3 = "Apple";
        result = SoundexUtils.difference(encoder, s1, s3);
        assertNull(result);

        // Test with long strings
        String longString1 = "The quick Brown Fox jumps over the lazy Dog";
        String longString2 = "The quick Red Fox jumps over the lazy Dog";
        int longStringResult = SoundexUtils.difference(encoder, longString1, longString2);
        assertEquals(4, longStringResult);
    }

    /**
     * Test case for the differenceEncoded method with RefinedSoundex encoder.
     */
    @Test
    public void testDifferenceEncodedRefinedSoundex() {
        String es1 = "A123";
        String es2 = "B456";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "A123";
        es2 = "A123";
        expected = 4;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "A123";
        es2 = "A124";
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, result);

        es1 = null;
        String es3 = "A123";
        result = SoundexUtils.differenceEncoded(es1, es3);
        assertNull(result);

        // Test with long strings
        String longEs1 = "T12345678901234567890123456789";
        String longEs2 = "T012345678901234567890123456789";
        int longEsResult = SoundexUtils.differenceEncoded(longEs1, longEs2);
        assertEquals(4, longEsResult);
    }
}