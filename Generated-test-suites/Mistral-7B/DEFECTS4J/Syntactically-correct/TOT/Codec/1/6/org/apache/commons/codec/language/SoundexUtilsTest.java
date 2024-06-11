package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for SoundexUtils.
 */
public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // typical use case
        String s = "Hello World";
        String cleanString = SoundexUtils.clean(s);
        String expectedCleanString = "HELLO";
        assertEquals(expectedCleanString, cleanString);

        // edge case: empty string
        String emptyString = "";
        String expectedEmptyString = "";
        assertEquals(expectedEmptyString, SoundexUtils.clean(emptyString));

        // error case: null string
        String nullString = null;
        String expectedNullString = null;
        assertEquals(expectedNullString, SoundexUtils.clean(nullString));
    }

    @Test
    public void testDifference() throws EncoderException {
        // typical use case
        String encoderType = "Soundex";
        String string1 = "Apple";
        String string2 = "Orange";
        int difference = SoundexUtils.difference(new SoundexEncoder(), string1, string2);
        int expectedDifference = 0;
        assertEquals(expectedDifference, difference);

        string1 = "Apple";
        string2 = "Apple";
        difference = SoundexUtils.difference(new SoundexEncoder(), string1, string2);
        int expectedMaxDifference = 4;
        assertEquals(expectedMaxDifference, difference);

        // edge case: empty strings
        String emptyString1 = "";
        String emptyString2 = "";
        difference = SoundexUtils.difference(new SoundexEncoder(), emptyString1, emptyString2);
        assertEquals(expectedMaxDifference, difference);

        // error case: null strings
        String nullString1 = null;
        String nullString2 = null;
        try {
            difference = SoundexUtils.difference(new SoundexEncoder(), nullString1, nullString2);
            fail();
        } catch (EncoderException e) {
            // expected
        }
    }

    @Test
    public void testDifferenceEncoded() {
        // typical use case
        String encodedString1 = "12345";
        String encodedString2 = "54321";
        int difference = SoundexUtils.differenceEncoded(encodedString1, encodedString2);
        int expectedDifference = 0;
        assertEquals(expectedDifference, difference);

        encodedString1 = "12345";
        encodedString2 = "12345";
        difference = SoundexUtils.differenceEncoded(encodedString1, encodedString2);
        int expectedMaxDifference = encodedString1.length();
        assertEquals(expectedMaxDifference, difference);

        // edge case: empty encoded strings
        String emptyEncodedString1 = "";
        String emptyEncodedString2 = "";
        difference = SoundexUtils.differenceEncoded(emptyEncodedString1, emptyEncodedString2);
        assertEquals(expectedMaxDifference, difference);

        // error case: null encoded strings
        String nullEncodedString1 = null;
        String nullEncodedString2 = null;
        try {
            difference = SoundexUtils.differenceEncoded(nullEncodedString1, nullEncodedString2);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }
}