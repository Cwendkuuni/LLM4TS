package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the Soundex class.
 */
public class SoundexTest {

    /**
     * Test encoding and decoding of simple strings.
     */
    @Test
    public void testSimpleEncodingAndDecoding() throws EncoderException {
        Soundex soundex = new Soundex();
        String input = "apple";
        String expectedOutput = "A113";
        String output = soundex.encode(input);
        Assert.assertEquals(expectedOutput, output);
        Assert.assertEquals(input, soundex.decode(output));
    }

    /**
     * Test encoding and decoding of strings with multiple letters.
     */
    @Test
    public void testMultipleLettersEncodingAndDecoding() throws EncoderException {
        Soundex soundex = new Soundex();
        String input = "banana";
        String expectedOutput = "B220";
        String output = soundex.encode(input);
        Assert.assertEquals(expectedOutput, output);
        Assert.assertEquals(input, soundex.decode(output));
    }

    /**
     * Test encoding and decoding of strings containing digits and special characters.
     */
    @Test
    public void testEncodingAndDecodingWithDigitsAndSpecialCharacters() throws EncoderException {
        Soundex soundex = new Soundex();
        String input = "Apple123$#";
        String expectedOutput = "A100";
        String output = soundex.encode(input);
        String decodedOutput = soundex.decode(output);
        Assert.assertEquals(expectedOutput, output);
        Assert.assertEquals(input, decodedOutput);
    }

    /**
     * Test encoding of empty string.
     */
    @Test
    public void testEncodingEmptyString() {
        Soundex soundex = new Soundex();
        String input = "";
        String expectedOutput = "";
        try {
            String output = soundex.encode(input);
            Assert.assertEquals(expectedOutput, output);
        } catch (EncoderException e) {
            // Expected since the encode method throws an exception for empty strings
        }
    }

    /**
     * Test decoding of invalid Soundex code.
     */
    @Test(expected = EncoderException.class)
    public void testDecodingInvalidSoundexCode() throws EncoderException {
        Soundex soundex = new Soundex();
        String soundexCode = "X111";
        soundex.decode(soundexCode);
    }

    /**
     * Test decoding of valid Soundex code with different input strings.
     */
    @Test
    public void testDecodingValidSoundexCodesWithDifferentInputs() throws EncoderException {
        Soundex soundex = new Soundex();
        String soundexCodeA = "A113";
        String expectedStringA = "apple";
        String stringA = soundex.decode(soundexCodeA);
        Assert.assertEquals(expectedStringA, stringA);

        String soundexCodeB = "B220";
        String expectedStringB = "banana";
        String stringB = soundex.decode(soundexCodeB);
        Assert.assertEquals(expectedStringB, stringB);
    }
}