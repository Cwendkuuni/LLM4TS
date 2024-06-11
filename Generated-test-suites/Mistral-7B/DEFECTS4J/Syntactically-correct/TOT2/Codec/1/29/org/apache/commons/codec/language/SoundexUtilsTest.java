
// SoundexUtilsTest.java (Complete)

import org.apache.commons.codec.encoder.UTF8Soundex;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "The quick Brown fox jumps over the lazy dog";
        String expected = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
        assertEquals(expected, SoundexUtils.clean(input));

        String emptyString = "";
        assertEquals(emptyString, SoundexUtils.clean(emptyString));

        String nullString = null;
        assertNull(SoundexUtils.clean(nullString));

        String inputWithSpecialCharacters = "The quick Brown fox jumps over the lazy d0g!";
        String expectedWithSpecialCharacters = "TH* QUICK BROWN FOX JUMPS OVER THE LAZY DOG!";
        assertEquals(expectedWithSpecialCharacters, SoundexUtils.clean(inputWithSpecialCharacters));
    }

    @Test
    public void testDifference() throws Exception {
        StringEncoder encoder = new UTF8Soundex();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        String s1a = "Apple";
        String s2a = "Apple";
        int actual = 4;
        result = SoundexUtils.difference(encoder, s1a, s2a);
        assertEquals(actual, result);

        StringEncoder nullEncoder = null;
        try {
            SoundexUtils.difference(nullEncoder, s1a, s2a);
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "12345";
        String es2 = "54321";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        String es1b = "123";
        String es2b = "123";
        int actual = 3;
        result = SoundexUtils.differenceEncoded(es1b, es2b);
        assertEquals(actual, result);

        String es1c = null;
        String es2c = "ABCDEFG";
        int expectedEncodedNull = 0;
        int resultEncodedNull = SoundexUtils.differenceEncoded(es1c, es2c);
        assertEquals(expectedEncodedNull, resultEncodedNull);

        String es1d = "12345";
        String es2d = "123456";
        int actualEncodedDifferentLength = 5;
        result = SoundexUtils.differenceEncoded(es1d, es2d);
        assertEquals(actualEncodedDifferentLength, result);
    }
}