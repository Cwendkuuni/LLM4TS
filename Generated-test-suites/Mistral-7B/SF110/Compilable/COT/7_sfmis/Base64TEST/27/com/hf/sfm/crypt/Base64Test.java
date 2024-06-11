package com.hf.sfm.crypt;
import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 22, 23, 24, 25};
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] output = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, output);
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidLength() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        try {
            Base64.altBase64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAltBase64ToByteArrayWithOnlyPadding() {
        String input = "==============";
        try {
            Base64.altBase64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        String input = "JywQ";
        byte[] output = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, output);
    }

    @Test
    public void testBase64ToByteArrayWithInvalidLength() {
        String input = "JywQ=";
        try {
            Base64.base64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64ToByteArrayWithOnlyPadding() {
        String input = "=======";
        try {
            Base64.base64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] input = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
        String output = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testByteArrayToBase64() {
        String expected = "JywQ";
        byte[] input = {'0', '1', '2', '3', '4', '5', '6', '7'};
        String output = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testByteArrayToBase64WithLengthLessThanThree() {
        byte[] input = {0};
        try {
            Base64.byteArrayToBase64(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}