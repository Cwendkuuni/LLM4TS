package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the SoundexUtils class.
 */
public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Test empty string
        String input1 = "";
        String expected1 = "";
        String actual1 = SoundexUtils.clean(input1);
        Assert.assertEquals(expected1, actual1);

        // Test null input
        String input2 = null;
        String expected2 = null;
        String actual2 = SoundexUtils.clean(input2);
        Assert.assertEquals(expected2, actual2);

        // Test single character
        String input3 = "A";
        String expected3 = "A";
        String actual3 = SoundexUtils.clean(input3);
        Assert.assertEquals(expected3, actual3);

        // Test multiple characters
        String input4 = "Hello World";
        String expected4 = "HELLO";
        String actual4 = SoundexUtils.clean(input4);
        Assert.assertEquals(expected4, actual4);

        // Test special characters
        String input5 = "123A!@#$%^&*()_+=[]{};:<>|./?-,";
        String expected5 = "";
        String actual5 = SoundexUtils.clean(input5);
        Assert.assertEquals(expected5, actual5);
    }

    @Test
    public void testDifference() throws EncoderException {
        // Test empty strings
        String encoderType = "Soundex";
        String input1 = "";
        String input2 = "";
        int expected = 4;
        int actual = SoundexUtils.difference(encoderType, input1, input2);
        Assert.assertEquals(expected, actual);

        // Test null inputs
        String input3 = null;
        String input4 = "Soundex";
        Assert.assertThrows(NullPointerException.class, () -> SoundxUtils.difference(encoderType, input3, input4));
        String input5 = "RefinedSoundex";
        Assert.assertThrows(NullPointerException.class, () -> SoundxUtils.difference(encoderType, input4, input5));

        // Test single character strings
        String input6 = "A";
        String input7 = "A";
        int expected6 = 4;
        int actual6 = SoundexUtils.difference(encoderType, input6, input7);
        Assert.assertEquals(expected6, actual6);

        // Test identical strings
        String input8 = "Apple";
        String input9 = "Apple";
        int expected8 = 4;
        int actual8 = SoundexUtils.difference(encoderType, input8, input9);
        Assert.assertEquals(expected8, actual8);

        // Test different strings with similar soundex codes
        String input10 = "Apple";
        String input11 = "Orange";
        int expected10 = 3;
        int actual10 = SoundexUtils.difference(encoderType, input10, input11);
        Assert.assertEquals(expected10, actual10);

        // Test different strings with different soundex codes
        String input12 = "Cat";
        String input13 = "Dog";
        int expected12 = 0;
        int actual12 = SoundexUtils.difference(encoderType, input12, input13);
        Assert.assertEquals(expected12, actual12);
    }

    @Test
    public void testDifferenceEncoded() {
        // Test empty strings
        String input1 = "";
        String input2 = "";
        int expected = 4;
        int actual = SoundexUtils.differenceEncoded(input1, input2);
        Assert.assertEquals(expected, actual);

        // Test null inputs
        String input3 = null;
        String input4 = "";
        Assert.assertThrows(NullPointerException.class, () -> SoundxUtils.differenceEncoded(input3, input4));
        String input5 = "";
        Assert.assertThrows(NullPointerException.class, () -> SoundxUtils.differenceEncoded(input4, input5));

        // Test single character strings
        String input6 = "A";
        String input7 = "A";
        int expected6 = 4;
        int actual6 = SoundexUtils.differenceEncoded(input6, input7);
        Assert.assertEquals(expected6, actual6);

        // Test identical strings
        String input8 = "Apple";
        String input9 = "Apple";
        String encoded1 = "AP000";
        String encoded2 = "AP000";
        int expected8 = 4;
        int actual8 = SoundexUtils.differenceEncoded(encoded1, encoded2);
        Assert.assertEquals(expected8, actual8);

        // Test different strings with similar soundex codes
        String input10 = "Apple";
        String input11 = "Orange";
        String encoded11 = "AP123";
        int expected10 = 3;
        int actual10 = SoundexUtils.differenceEncoded(encoded1, encoded11);
        Assert.assertEquals(expected10, actual10);

        // Test different strings with different soundex codes
        String input12 = "Cat";
        String input13 = "Dog";
        String encoded12 = "CA100";
        String encoded13 = "DO110";
        int expected12 = 0;
        int actual12 = SoundexUtils.differenceEncoded(encoded12, encoded13);
        Assert.assertEquals(expected12, actual12);
    }
}