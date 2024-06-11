package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundexUSEnglishEncoding() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple, Application, ApplicationSales, AppleInc, Approach, Approval, Approve, Approver, Approximately, April, Are, Around, As, Ask, Assume, Assist, Assistance, At, Before, Because, Been, Between, Both, But, By";
        String expected = "A123";
        String result = soundex.encode(input);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testSoundexEncodingWithNonStringInput() {
        Soundex soundex = Soundex.US_ENGLISH;
        Object nonStringInput = 123;
        try {
            String encoded = soundex.encode(nonStringInput);
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testSoundexEncodingEmptyString() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "";
        String expected = "";
        String result = soundex.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testSoundexEncodingSingleCharacter() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "A";
        String expected = "A000";
        String result = soundex.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testSoundexEncodingTwoSameLetters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "AA";
        String expected = "A100";
        String result = soundex.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testSoundexEncodingTwoDifferentLetters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "AB";
        String expected = "A120";
        String result = soundex.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testSoundexEncodingStringWithDigits() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "ApplicationSales123";
        String expected = "A123";
        String result = soundex.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testSoundexEncodingStringWithSpecialCharacters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple,!@#% Application,@#$% ApplicationSales,Apple$% Inc,@#Approach,@#Approval,@#Approve,@#Approver,@#Approximately,@#April,@#Are,@#Around,@#As,@#Ask,@#Assume,@#Assist,@#Assistance,@#At,@#Before,@#Because,@#Been,@#Between,@#Both,@#But,@#By";
        String expected = "A123";
        String result = soundex.encode(input);
        assertEquals(expected, result);
    }
}