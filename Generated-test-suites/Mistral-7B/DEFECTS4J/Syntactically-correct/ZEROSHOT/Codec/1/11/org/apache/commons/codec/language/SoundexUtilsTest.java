package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;

import org.junit.Test;

/**
 * Test class for the SoundexUtils class.
 */
public class SoundexUtilsTest {

    /**
     * Test the clean method.
     */
    @Test
    public void testClean() {
        String input = null;
        String expected = null;
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "";
        expected = "";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "abcd";
        expected = "ABCD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "abCD";
        expected = "ABCD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "ab cD";
        expected = "ABCD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "a BC dE";
        expected = "ABCD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "A B C D";
        expected = "ABCD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = " abcdefghijklmnopqrstuvwxyz";
        expected = "";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "1234abcd";
        expected = "ABCD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "ABCdEfGhIjKlMnOpQrStUvWxYz1234";
        expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test the difference method.
     */
    @Test(expected = EncoderException.class)
    public void testDifferenceWithNullEncoder() {
        String encoderName = null;
        String s1 = "apple";
        String s2 = "orange";
        SoundexUtils.difference(null, s1, s2);
    }

    @Test
    public void testDifference() throws EncoderException {
        String encoderName = "Soundex";
        String s1 = "apple";
        String s2 = "aple";
        int result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(3, result);

        s1 = "apple";
        s2 = "orange";
        result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(0, result);

        s1 = "apple";
        s2 = "aple";
        result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(3, result);

        s1 = "apple";
        s2 = "apples";
        result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(3, result);

        s1 = "apple";
        s2 = "apples";
        result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(3, result);

        s1 = "Apple";
        s2 = "Apples";
        result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(3, result);

        s1 = "Apple";
        s2 = "Apples";
        result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(3, result);

        s1 = "Apple";
        s2 = "Apples";
        Soundex soundex1 = new Soundex();
        String soundexS1 = soundex1.encode(s1);
        String soundexS2 = soundex1.encode(s2);
        result = SoundexUtils.differenceEncoded(soundexS1, soundexS2);
        assertEquals(3, result);

        s1 = "Apple";
        s2 = "Apples";
        Soundex soundex1_1 = new Soundex();
        String soundexS1_1 = soundex1_1.encode(s1);
        String soundexS2_1 = soundex1_1.encode(s2);
        result = SoundexUtils.differenceEncoded(soundexS1_1, soundexS2_1);
        assertEquals(3, result);

        s1 = "Apple";
        s2 = "Orange";
        result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(0, result);

        s1 = "Apple";
        s2 = "Orange";
        result = SoundexUtils.difference(new RefinedSoundex(), s1, s2);
        assertEquals(0, result);

        s1 = "Apple";
        s2 = "Orange";
        Soundex soundex1_2 = new Soundex();
        String soundexS1_2 = soundex1_2.encode(s1);
        String soundexS2_2 = soundex1_2.encode(s2);
        result = SoundexUtils.differenceEncoded(soundexS1_2, soundexS2_2);
        assertEquals(0, result);

        s1 = "Apple";
        s2 = "Orange";
        Soundex soundex1_3 = new Soundex();
        String soundexS1_3 = soundex1_3.encode(s1);
        String soundexS2_3 = soundex1_3.encode(s2);
        result = SoundexUtils.differenceEncoded(soundexS1_3, soundexS2_3);
        assertEquals(0, result);

        s1 = "Apple";
        s2 = "";
        try {
            result = SoundexUtils.difference(new Soundex(), s1, s2);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }

        s1 = "Apple";
        s2 = null;
        try {
            result = SoundexUtils.difference(new Soundex(), s1, s2);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }

        s1 = "";
        s2 = "Apple";
        try {
            result = SoundexUtils.difference(new Soundex(), s1, s2);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }

        s1 = null;
        s2 = "Apple";
        try {
            result = SoundexUtils.difference(new Soundex(), s1, s2);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }

        s1 = "";
        s2 = "";
        result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(0, result);

        s1 = "Apple";
        s2 = "";
        result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(0, result);

        s1 = "";
        s2 = "Apple";
        result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(0, result);

        s1 = "";
        s2 = "";
        result = SoundexUtils.difference(new RefinedSoundex(), s1, s2);
        assertEquals(0, result);
    }

    /**
     * Test the clean and difference methods together.
     */
    @Test
    public void testCleanAndDifference() {
        String s1 = "Apple";
        String s2 = "Orange";
        String encoderName = "Soundex";
        String cleanS1 = null;
        String cleanS2 = null;
        String encodedS1 = null;
        String encodedS2 = null;
        int difference = 0;

        try {
            cleanS1 = SoundexUtils.clean(s1);
            encodedS1 = SoundexUtils.difference(new Soundex(), cleanS1, null)[0];

            cleanS2 = SoundexUtils.clean(s2);
            encodedS2 = SoundexUtils.difference(new Soundex(), cleanS2, null)[0];

            difference = SoundexUtils.differenceEncoded(encodedS1, encodedS2);
            assertEquals(0, difference);

            s1 = "apple";
            cleanS1 = SoundexUtils.clean(s1);
            encodedS1 = SoundexUtils.difference(new Soundex(), cleanS1, null)[0];

            cleanS2 = SoundexUtils.clean(s2);
            encodedS2 = SoundexUtils.difference(new Soundex(), cleanS2, null)[0];

            difference = SoundexUtils.differenceEncoded(encodedS1, encodedS2);
            assertEquals(3, difference);
        } catch (EncoderException e) {
            fail("Unexpected EncoderException");
        }

        try {
            cleanS1 = SoundexUtils.clean(s1);
            encodedS1 = SoundexUtils.difference(new Soundex(), cleanS1, null)[0];

            cleanS2 = SoundexUtils.clean(s2);
            encodedS2 = SoundexUtils.difference(new RefinedSoundex(), cleanS2, null)[0];

            difference = SoundexUtils.differenceEncoded(encodedS1, encodedS2);
            assertEquals(0, difference);
        } catch (EncoderException e) {
            fail("Unexpected EncoderException");
        }
    }
}