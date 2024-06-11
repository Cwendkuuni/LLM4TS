package org.junit.tests.soundexutils;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        String input = "HelloWorld!";
        String expectedOutput = "HELLO";
        String output = SoundexUtils.clean(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCleanNullString() {
        String input = null;
        String output = SoundexUtils.clean(input);
        assertNull(output);
    }

    @Test
    public void testCleanEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = SoundexUtils.clean(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCleanStringWithOnlyDigits() {
        String input = "12345";
        String expectedOutput = "";
        String output = SoundexUtils.clean(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCleanStringWithOnlySpecialCharacters() {
        String input = "!@#$%^&*()_+-=[]{}|;:,.<>?/~`";
        String expectedOutput = "";
        String output = SoundexUtils.clean(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceNullEncoder() {
        String s1 = "Apple";
        String s2 = "Orange";
        StringEncoder encoder = null;
        int output = SoundexUtils.difference(encoder, s1, s2);
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceNullString1() {
        StringEncoder encoder = new org.apache.commons.codec.StringEncoder();
        String s1 = null;
        String s2 = "Orange";
        int output = SoundexUtils.difference(encoder, s1, s2);
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceNullString2() {
        StringEncoder encoder = new org.apache.commons.codec.StringEncoder();
        String s1 = "Apple";
        String s2 = null;
        int output = SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceSameStrings() {
        StringEncoder encoder = new org.apache.commons.codec.StringEncoder();
        String s1 = "Apple";
        String s2 = "Apple";
        int expectedOutput = 4;
        int output = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testDifferenceOneCharacterDifference() {
        StringEncoder encoder = new org.apache.commons.codec.StringEncoder();
        String s1 = "Apple";
        String s2 = "Orange";
        int expectedOutput = 1;
        int output = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testDifferenceMoreThanOneCharacterDifference() {
        StringEncoder encoder = new org.apache.commons.codec.StringEncoder();
        String s1 = "Apple";
        String s2 = "Banana";
        int expectedOutput = 1;
        int output = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncodedStringsNull() {
        String es1 = null;
        String es2 = "ORNG";
        int output = SoundexUtils.differenceEncoded(es1, es2);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncodedStringsDifferentLengths() {
        String es1 = "AEPPL";
        String es2 = "ORNG";
        int output = SoundexUtils.differenceEncoded(es1, es2);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncodedStringsDifferentCharacters() {
        String es1 = "AEPPL";
        String es2 = "ORNGX";
        int output = SoundexUtils.differenceEncoded(es1, es2);
    }

    @Test
    public void testDifferenceEncodedStringsSameLength() {
        String es1 = "AEPPL";
        String es2 = "AEPPL";
        int expectedOutput = 4;
        int output = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testDifferenceEncodedStringsOneCharacterDifference() {
        String es1 = "AEPPL";
        String es2 = "AEPPQ";
        int expectedOutput = 1;
        int output = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, output);
    }
}