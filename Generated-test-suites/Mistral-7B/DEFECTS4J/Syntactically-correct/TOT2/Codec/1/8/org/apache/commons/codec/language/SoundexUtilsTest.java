package org.junit.tests.soundexutils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.encoder.UpperCase;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "The quick Brown Fox jumps Over the lazy Dog";
        String expectedOutput = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCleanMethodWithEmptyString() {
        String input = "";
        String expectedOutput = "";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCleanMethodWithNullInput() {
        String input = null;
        String expectedOutput = null;
        String actualOutput = SoundexUtils.clean(input);
        assertNull(actualOutput);
    }

    @Test
    public void testDifferenceMethodWithSoundexEncoderAndSameStrings() throws EncoderException {
        StringEncoder encoder = new UpperCase();
        String s1 = "Apple";
        String s2 = "Apple";
        int expectedOutput = 4; // Maximum match for Soundex
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceMethodWithSoundexEncoderAndDifferentStrings() throws EncoderException {
        StringEncoder encoder = new UpperCase();
        String s1 = "Apple";
        String s2 = "Orange";
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceMethodWithInvalidInput() throws EncoderException {
        StringEncoder encoder = new UpperCase();
        String s1 = "Apple";
        String s2 = null;
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceEncodedMethodWithSameEncodedStrings() {
        String es1 = "122223";
        String es2 = "122223";
        int expectedOutput = 4; // Maximum match for Soundex
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncodedMethodWithDifferentEncodedStrings() {
        String es1 = "122223";
        String es2 = "111112";
        int expectedOutput = 1;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }
}