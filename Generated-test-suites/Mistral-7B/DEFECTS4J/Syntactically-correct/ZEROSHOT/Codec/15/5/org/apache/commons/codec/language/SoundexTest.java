package org.apache.commons.codec.language;

import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;

/**
 * Test class for Soundex.java
 *
 * @author [Your Name]
 */
public class SoundexTest {

    @Test
    public void testSoundexUSEnglishMapping() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "apple apple sauce apple pie";
        String expected = "A111 A111 A211 A111";
        String result = soundex.soundex(input);
        assertEquals(expected, result);
    }

    @Test
    public void testSoundexCustomMapping() {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Soundex soundex = new Soundex(customMapping);
        String input = "apple apple sauce apple pie";
        String expected = "A111 A111 A211 A111";
        String result = soundex.soundex(input);
        assertEquals(expected, result);
    }

    @Test
    public void testSoundexDifference() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;
        String str1 = "apple apple sauce apple pie";
        String str2 = "aple aple sauce apple pie";
        int difference = soundex.difference(str1, str2);
        assertEquals(3, difference);
    }

    @Test
    public void testSoundexEncode() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "apple apple sauce apple pie";
        String expected = "A111 A111 A211 A111";
        String result = soundex.encode(input).toString();
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testSoundexEncodeInvalidInput() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;
        int number = 123;
        String result = soundex.encode(number).toString();
        fail("Expected EncoderException but received: " + result);
    }
}