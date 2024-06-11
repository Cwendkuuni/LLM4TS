package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input1 = "The quick Brown fox jumps over the lazy dog";
        String expected = "The quick Brown Fox Jumps Over The Lazy Dog";
        String result = SoundexUtils.clean(input1);
        assertEquals(expected, result);
    }

    @Test
    public void testCleanWithSpecialCharacters() {
        String input2 = "The quick Brown fox! jumps over the lazy dog@";
        String expected = "The quick Brown Fox Jumps Over The Lazy Dog";
        String result = SoundexUtils.clean(input2);
        assertEquals(expected, result);
    }

    @Test
    public void testCleanWithDigits() {
        String input3 = "The quick Brown fox jumps over the lazy dog12345";
        String expected = "The quick Brown Fox Jumps Over The Lazy Dog";
        String result = SoundexUtils.clean(input3);
        assertEquals(expected, result);
    }

    // Test for clean method - Add more test cases as needed

    @Test
    public void testDifferenceNullStrings() {
        StringEncoder encoder = new org.apache.commons.codec.net.URLCodec();
        String s1 = null;
        String s2 = null;
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceWithEmptyStrings() {
        StringEncoder encoder = new org.apache.commons.codec.net.URLCodec();
        String s1 = "";
        String s2 = "";
        int expected = 4;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceWithDifferentLengthStrings() {
        StringEncoder encoder = new org.apache.commons.codec.net.URLCodec();
        String s1 = "hello";
        String s2 = "world";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceWithIdenticalStrings() {
        StringEncoder encoder = new org.apache.commons.codec.net.URLCodec();
        String s1 = "hello";
        String s2 = "hello";
        int expected = 4;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceWithTwoStringsHavingDifferentFirstLetters() {
        StringEncoder encoder = new org.apache.commons.codec.net.URLCodec();
        String s1 = "hello";
        String s2 = "world";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceWithTwoStringsHavingSameFirstLettersButDifferentFollowingLetters() {
        StringEncoder encoder = new org.apache.commons.codec.net.URLCodec();
        String s1 = "apple";
        String s2 = "apples";
        int expected = 3;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceWithTwoStringsHavingSameFirstLettersAndSameFollowingLetters() {
        StringEncoder encoder = new org.apache.commons.codec.net.URLCodec();
        String s1 = "hello";
        String s2 = "hello";
        int expected = 4;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    // Add more test cases as needed

}