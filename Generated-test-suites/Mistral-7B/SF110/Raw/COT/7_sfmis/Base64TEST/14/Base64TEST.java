package com.hf.sfm.crypt.test;

import org.junit.Assert;
import org.junit.Test;

import com.hf.sfm.crypt.Base64;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray_validInput() {
        String validInput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedOutput = Base64._$23169;
        byte[] output = Base64.altBase64ToByteArray(validInput);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidInput_stringLengthNotMultipleOfFour() {
        String invalidInput = "ABCDEF";
        Base64.altBase64ToByteArray(invalidInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidInput_illegalCharacter() {
        String invalidInput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/x";
        Base64.altBase64ToByteArray(invalidInput);
    }

    @Test
    public void testAltBase64ToByteArray_validInputWithTrailingEquals() {
        String validInputWithTrailingEquals = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        byte[] expectedOutput = Base64._$23169;
        byte[] output = Base64.altBase64ToByteArray(validInputWithTrailingEquals);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testAltBase64ToByteArray_validInputWithOneTrailingEqual() {
        String validInputWithOneTrailingEqual = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        byte[] expectedOutput = Base64._$23169;
        byte[] output = Base64.altBase64ToByteArray(validInputWithOneTrailingEqual.substring(0, validInputWithOneTrailingEqual.length() - 1));
        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testAltBase64ToByteArray_validInputWithNoTrailingEquals() {
        String validInputWithNoTrailingEquals = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedOutput = Base64._$23169;
        byte[] output = Base64.altBase64ToByteArray(validInputWithNoTrailingEquals);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testBase64ToByteArray_validInput() {
        String validInput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedOutput = Base64._$23169;
        byte[] output = Base64.base64ToByteArray(validInput);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidInput_stringLengthNotMultipleOfFour() {
        String invalidInput = "ABCDEF";
        Base64.base64ToByteArray(invalidInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidInput_illegalCharacter() {
        String invalidInput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/x";
        Base64.base64ToByteArray(invalidInput);
    }

    @Test
    public void testByteArrayToAltBase64_validInput() {
        byte[] input = Base64._$23169;
        String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String output = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToAltBase64_validInputWithNoPadding() {
        byte[] input = Base64._$23168;
        String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String output = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToAltBase64_validInputWithPadding() {
        byte[] input = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 63, 63, 63 };
        String expectedOutput = "U2lzYW5jZQ==";
        String output = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64_validInput() {
        byte[] input = Base64._$23169;
        String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String output = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64_validInputWithNoPadding() {
        byte[] input = Base64._$23168;
        String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String output = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64_validInputWithPadding() {
        byte[] input = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 65, 65, 65 };
        String expectedOutput = "VGltZXN0IGZvdWwgdG8=";
        String output = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }
}