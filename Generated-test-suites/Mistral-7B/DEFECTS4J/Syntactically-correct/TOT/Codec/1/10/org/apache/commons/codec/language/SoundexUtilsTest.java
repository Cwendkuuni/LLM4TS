package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link SoundexUtils}.
 */
public class SoundexUtilsTest {

    /**
     * Test for {@link SoundexUtils#clean(String)} method.
     */
    @Test
    public void testClean() {
        String input = "The quick Brown Fox jumps over the lazy Dog";
        String expected = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
        String result = SoundexUtils.clean(input);
        Assert.assertEquals(expected, result);

        String emptyString = "";
        String resultEmpty = SoundexUtils.clean(emptyString);
        Assert.assertEquals(emptyString, resultEmpty);

        String nullString = null;
        String resultNull = SoundexUtils.clean(nullString);
        Assert.assertNull(resultNull);
    }

    /**
     * Test for {@link SoundexUtils#difference(StringEncoder, String, String)} method.
     */
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.Decoder();
        int diff = SoundexUtils.difference(encoder, "Apple", "Orange");
        Assert.assertEquals(0, diff);

        String s1 = "Soundex";
        String s2 = "SounDex";
        int expected = 3;
        int result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, result);

        String s3 = "Apple";
        String s4 = "Orange";
        int resultDifference = SoundexUtils.difference(encoder, s3, s4);
        Assert.assertEquals(0, resultDifference);
    }

    /**
     * Test for {@link SoundexUtils#differenceEncoded(String, String)} method.
     */
    @Test
    public void testDifferenceEncoded() {
        String es1 = "T3211";
        String es2 = "T2211";
        int expected = 2;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, result);

        String es3 = "S2000";
        String es4 = "S2000";
        int resultDifferenceEncoded = SoundexUtils.differenceEncoded(es3, es4);
        Assert.assertEquals(4, resultDifferenceEncoded);

        String es5 = "A1000";
        String es6 = "A1000";
        int resultDifferenceEncoded2 = SoundexUtils.differenceEncoded(es5, es6);
        Assert.assertEquals(4, resultDifferenceEncoded2);
    }
}