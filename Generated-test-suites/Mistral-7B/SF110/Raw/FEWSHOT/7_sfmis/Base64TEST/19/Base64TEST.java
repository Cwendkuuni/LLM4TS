package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedByteArray = new byte[26];
        for (int i = 0; i < 26; i++) {
            expectedByteArray[i] = (byte) i;
        }

        byte[] resultByteArray = Base64.altBase64ToByteArray(testString);
        Assert.assertArrayEquals("altBase64ToByteArray should return expected byte array", expectedByteArray, resultByteArray);
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidLength() {
        String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Assert.throwsException(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(testString));
    }

    @Test
    public void testAltBase64ToByteArrayWithTrailingEquals() {
        String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ==";
        byte[] resultByteArray = Base64.altBase64ToByteArray(testString);
        byte[] expectedByteArray = new byte[26];
        System.arraycopy(Base64._$23169, 0, expectedByteArray, 0, 24);
        expectedByteArray[24] = 24;
        Assert.assertArrayEquals("altBase64ToByteArray should return expected byte array with trailing equals", expectedByteArray, resultByteArray);
    }

    @Test
    public void testAltBase64ToByteArrayWithTrailingEqualsAndInvalidLength() {
        String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ=== ";
        Assert.throwsException(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(testString));
    }

    @Test
    public void testBase64ToByteArray() {
        String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedByteArray = new byte[26];
        for (int i = 0; i < 26; i++) {
            expectedByteArray[i] = (byte) i;
        }

        byte[] resultByteArray = Base64.base64ToByteArray(testString);
        Assert.assertArrayEquals("base64ToByteArray should return expected byte array", expectedByteArray, resultByteArray);
    }

    @Test
    public void testBase64ToByteArrayWithInvalidCharacter() {
        String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/x";
        Assert.throwsException(IllegalArgumentException.class, () -> Base64.base64ToByteArray(testString));
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] testByteArray = new byte[26];
        for (int i = 0; i < 26; i++) {
            testByteArray[i] = (byte) i;
        }

        String resultString = Base64.byteArrayToAltBase64(testByteArray);
        String expectedString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        Assert.assertEquals("byteArrayToAltBase64 should return expected string", expectedString, resultString);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] testByteArray = new byte[26];
        for (int i = 0; i < 26; i++) {
            testByteArray[i] = (byte) i;
        }

        String resultString = Base64.byteArrayToBase64(testByteArray);
        String expectedString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        Assert.assertEquals("byteArrayToBase64 should return expected string", expectedString, resultString);
    }
}