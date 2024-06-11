package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundexUSEnglish() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;

        // Typical use case
        String str1 = "Apple";
        String expectedResult1 = "A231";
        String result1 = soundex.soundex(str1);
        assertEquals(expectedResult1, result1);

        // Typical use case
        String str2 = "Banana";
        String expectedResult2 = "B112";
        String result2 = soundex.soundex(str2);
        assertEquals(expectedResult2, result2);

        // Edge case: empty string
        String strEmpty = "";
        String expectedResultEmpty = "";
        String resultEmpty = soundex.soundex(strEmpty);
        assertEquals(expectedResultEmpty, resultEmpty);

        // Edge case: null input
        String strNull = null;
        String expectedResultNull = null;
        String resultNull = soundex.soundex(strNull);
        assertEquals(expectedResultNull, resultNull);

        // Error case: invalid input
        String strInvalid = "!@#$%^&*";
        String expectedResultInvalid = "";
        String resultInvalid = soundex.soundex(strInvalid);
        assertEquals(expectedResultInvalid, resultInvalid);

        // Error case: encoding failure
        String strEncodingFailure = "παππαππα";
        try {
            soundex.soundex(strEncodingFailure);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testSoundexCustomMapping() throws EncoderException {
        Soundex soundex = new Soundex(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '1', '2', '2', '3', '3', '4', '4'});

        // Typical use case
        String str1 = "Apple";
        String expectedResult1 = "A231";
        String result1 = soundex.soundex(str1);
        assertEquals(expectedResult1, result1);

        // Typical use case
        String str2 = "Banana";
        String expectedResult2 = "B112";
        String result2 = soundex.soundex(str2);
        assertEquals(expectedResult2, result2);

        // Edge case: empty string
        String strEmpty = "";
        String expectedResultEmpty = "";
        String resultEmpty = soundex.soundex(strEmpty);
        assertEquals(expectedResultEmpty, resultEmpty);

        // Edge case: null input
        String strNull = null;
        String expectedResultNull = null;
        String resultNull = soundex.soundex(strNull);
        assertEquals(expectedResultNull, resultNull);

        // Error case: invalid input
        String strInvalid = "!@#$%^&*";
        String expectedResultInvalid = "";
        String resultInvalid = soundex.soundex(strInvalid);
        assertEquals(expectedResultInvalid, resultInvalid);

        // Error case: encoding failure
        String strEncodingFailure = "παππαππα";
        try {
            soundex.soundex(strEncodingFailure);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }
}