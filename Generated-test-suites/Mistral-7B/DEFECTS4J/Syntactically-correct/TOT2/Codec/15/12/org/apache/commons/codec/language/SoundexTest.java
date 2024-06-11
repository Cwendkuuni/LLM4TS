package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class SoundexTest {

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String str = "Apple";
        String expected = "K321";
        String actual = soundex.encode(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEncodeNullString() {
        Soundex soundex = new Soundex();
        String str = null;
        try {
            soundex.encode(str);
            Assert.fail();
        } catch (EncoderException e) {
            //expected
        }
    }

    @Test
    public void testEncodeEmptyString() {
        Soundex soundex = new Soundex();
        String str = "";
        String expected = str;
        String actual = soundex.encode(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEncodeMappingEdgeCases() {
        Soundex soundex = new Soundex();
        char[] mapping = new char[]{'B', 'C', '0'};
        Soundex soundexWithCustomMapping = new Soundex(mapping);
        String str = "AbbaBb";
        String expected = "B22";
        String actual = soundexWithCustomMapping.encode(str);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeInvalidCharacter() {
        Soundex soundex = new Soundex();
        String str = "Apple#";
        soundex.encode(str);
        //exception will be thrown here
    }

    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        String str1 = "Apple";
        String str2 = "Orange";
        int expected = 0;
        int actual = soundex.difference(str1, str2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceSameString() {
        Soundex soundex = new Soundex();
        String str1 = "Apple";
        String str2 = "Apple";
        int expected = 4;
        int actual = soundex.difference(str1, str2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceSameFirstThreeLetters() {
        Soundex soundex = new Soundex();
        String str1 = "Apple";
        String str2 = "Appleberry";
        int expected = 3;
        int actual = soundex.difference(str1, str2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceDifferentLength() {
        Soundex soundex = new Soundex();
        String str1 = "Apple";
        String str2 = "AppleAppleberry";
        int expected = 3;
        int actual = soundex.difference(str1, str2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRandomStrings() {
        Soundex soundex = new Soundex();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            String str1 = "String" + random.nextInt(100) + i;
            String str2 = "String" + random.nextInt(100) + i;
            int difference = soundex.difference(str1, str2);
            System.out.println("String " + str1 + " and String " + str2 + " have a difference of: " + difference);
        }
    }

}