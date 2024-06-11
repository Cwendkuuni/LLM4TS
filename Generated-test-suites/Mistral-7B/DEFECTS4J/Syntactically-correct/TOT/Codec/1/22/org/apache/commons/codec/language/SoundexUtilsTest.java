package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for SoundexUtils.
 */
public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "aBc 123!@#$%^&*()_Plus!negative-digits.And@special_Characters. WithSpace.";
        String expected = "ABC";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
        
        String s3 = "Apple";
        String s4 = "Apple";
        int difference = SoundexUtils.differenceEncoded(encoder.encode(s3), encoder.encode(s4));
        assertEquals(4, difference); // or more for refined Soundex
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithEmptyString() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "";
        String s2 = "Apple";
        SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithNullString() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = null;
        String s2 = "Apple";
        SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithNullEncoder() throws EncoderException {
        String s1 = "Apple";
        String s2 = "Apple";
        SoundexUtils.differenceEncoded(null, s1, s2);
    }

    @Test
    public void testCleanEdgeCases() {
        String input = "AaBbCc123!@#$%^&*()_+-=[]{}|\\:;\"'<>,.?/ ~`";
        String expected = "AABBCC";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }
}