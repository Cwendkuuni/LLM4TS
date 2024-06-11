package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = null;
        String expectedOutput = "";
        String actualOutput = SoundexUtils.clean(input);
        Assert.assertEquals(expectedOutput, actualOutput);

        input = "";
        expectedOutput = "";
        actualOutput = SoundexUtils.clean(input);
        Assert.assertEquals(expectedOutput, actualOutput);

        input = "a";
        expectedOutput = "A";
        actualOutput = SoundexUtils.clean(input);
        Assert.assertEquals(expectedOutput, actualOutput);

        input = "hello world";
        expectedOutput = "HELLO WORLD";
        actualOutput = SoundexUtils.clean(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCleanMethodWithEdgeCases() {
        String input = "  App1e";
        String expectedOutput = "APE";
        String actualOutput = SoundexUtils.clean(input);
        Assert.assertEquals(expectedOutput, actualOutput);

        input = " Apple";
        expectedOutput = "APPLE";
        actualOutput = SoundexUtils.clean(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceWithSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Apple";
        String s2 = "Orange";
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expectedOutput, actualOutput);

        s1 = "Smith";
        s2 = "Smyth";
        expectedOutput = 3;
        actualOutput = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceWithRefinedSoundexEncoderAndException() throws EncoderException {
        StringEncoder encoder = new RefinedSoundex();
        String s1 = null;
        String s2 = "Orange";
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, s1, s2));

        s1 = "Apple";
        s2 = null;
        expectedOutput = 0;
        actualOutput = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceWithSoundexEncoderAndEdgeCases() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Apple";
        String s2 = "Apple";
        int expectedOutput = 4;
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expectedOutput, actualOutput);

        s1 = "Apple";
        s2 = "Orange";
        expectedOutput = 0;
        actualOutput = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

}