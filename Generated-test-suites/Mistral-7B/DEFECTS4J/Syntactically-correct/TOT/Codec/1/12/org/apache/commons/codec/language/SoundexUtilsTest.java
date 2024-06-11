package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test for {@link SoundexUtils} class.
 */
public class SoundexUtilsTest {

    /**
     * Test for {@link SoundexUtils#clean(String)} method.
     */
    @Test
    public void testClean() {
        // Test empty string
        String emptyString = "";
        assertEquals("", SoundexUtils.clean(emptyString));

        // Test null string
        String nullString = null;
        assertEquals(null, SoundexUtils.clean(nullString));

        // Test simple case
        String inputString1 = "Apple";
        String expectedCleanString1 = "APP";
        assertEquals(expectedCleanString1, SoundexUtils.clean(inputString1));

        // Test multiple letters
        String inputString2 = "Banana";
        String expectedCleanString2 = "BAN";
        assertEquals(expectedCleanString2, SoundexUtils.clean(inputString2));

        // Test multiple words
        String inputString3 = "Hello World";
        String expectedCleanString3 = "HELLO WORLD";
        assertEquals(expectedCleanString3, SoundexUtils.clean(inputString3));

        // Test edge cases
        String inputString4 = "12345";
        String expectedCleanString4 = "";
        assertEquals(expectedCleanString4, SoundexUtils.clean(inputString4));

        String inputString5 = "ABC123";
        String expectedCleanString5 = "ABC";
        assertEquals(expectedCleanString5, SoundexUtils.clean(inputString5));

        String inputString6 = "hello there";
        String expectedCleanString6 = "HELLO THERE";
        assertEquals(expectedCleanString6, SoundexUtils.clean(inputString6));
    }

    /**
     * Test for {@link SoundexUtils#differenceEncoded(String, String)} method.
     * <p>
     * Note: This test method assumes that the {@link SoundexUtils#difference(StringEncoder, String, String)}
     * method is correctly implemented.
     */
    @Test
    public void testDifferenceEncoded() throws EncoderException {
        // Test empty strings
        StringEncoder encoder = org.apache.commons.codec.net.URLCodec.INSTANCE;
        String s1 = "";
        String s2 = "";
        int expectedDiff = 4;
        int actualDiff = SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
        assertEquals(expectedDiff, actualDiff);

        // Test null strings
        String s3 = null;
        String s4 = null;
        int expectedDiff2 = 4;
        int actualDiff2 = SoundexUtils.differenceEncoded(encoder.encode(s3), encoder.encode(s4));
        assertEquals(expectedDiff2, actualDiff2);

        // Test simple cases
        String s5 = "Apple";
        String s6 = "Apple";
        int expectedDiff3 = 4;
        int actualDiff3 = SoundexUtils.differenceEncoded(encoder.encode(s5), encoder.encode(s6));
        assertEquals(expectedDiff3, actualDiff3);

        String s7 = "Apple";
        String s8 = "Banana";
        int expectedDiff4 = 0;
        int actualDiff4 = SoundexUtils.differenceEncoded(encoder.encode(s7), encoder.encode(s8));
        assertEquals(expectedDiff4, actualDiff4);

        // Test multiple words
        String s9 = "Hello World";
        String s10 = "Hello Universe";
        int expectedDiff5 = 3;
        int actualDiff5 = SoundexUtils.differenceEncoded(encoder.encode(s9), encoder.encode(s10));
        assertEquals(expectedDiff5, actualDiff5);
    }
}