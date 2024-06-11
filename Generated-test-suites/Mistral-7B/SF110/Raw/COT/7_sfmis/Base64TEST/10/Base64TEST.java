package com.hf.sfm.crypt.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.junit.Test;

import com.hf.sfm.crypt.Base64;

public class Base64Test {

    // Test altBase64ToByteArray method

    @Test
    public void testAltBase64ToByteArray_ValidInput() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedByteArray = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
        byte[] resultByteArray = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, resultByteArray);
    }

    @Test
    public void testAltBase64ToByteArray_InputWithTrailingEquals() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+=";
        byte[] expectedByteArray = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
        byte[] resultByteArray = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, resultByteArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_InvalidInput() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Input length should be multiple of 4.
        Base64.altBase64ToByteArray(base64String);
    }

    // Test base64ToByteArray method

    @Test
    public void testBase64ToByteArray_ValidInput() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedByteArray = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
        byte[] resultByteArray = Base64.base64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, resultByteArray);
    }

    @Test
    public void testBase64ToByteArray_InputWithTrailingEquals() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+=";
        byte[] expectedByteArray = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
        byte[] resultByteArray = Base64.base64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, resultByteArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_InvalidInput() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Input length should be multiple of 4.
        Base64.base64ToByteArray(base64String);
    }

    // Test altBase64ToString method

    @Test
    public void testAltBase64ToString_ValidInput() {
        byte[] byteArray = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
        String expectedBase64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String resultBase64String = Base64.byteArrayToAltBase64(byteArray);
        assertEquals(expectedBase64String, resultBase64String);
    }

    @Test
    public void testAltBase64ToString_InputWithTrailingZeros() {
        byte[] byteArray = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
        String expectedBase64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+=";
        String resultBase64String = Base64.byteArrayToAltBase64(byteArray);
        assertEquals(expectedBase64String, resultBase64String);
    }

    @Test
    public void testAltBase64ToString_EmptyInput() {
        byte[] byteArray = new byte[0];
        String resultBase64String = Base64.byteArrayToAltBase64(byteArray);
        assertEquals("", resultBase64String);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToString_InvalidInput() {
        byte[] byteArray = new byte[] { 0, 1, 2, 3 }; // Input length should be a multiple of 3.
        Base64.byteArrayToAltBase64(byteArray);
    }

    // Test base64ToString method

    @Test
    public void testBase64ToString_ValidInput() {
        byte[] byteArray = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
        String expectedBase64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String resultBase64String = Base64.byteArrayToBase64(byteArray);
        assertEquals(expectedBase64String, resultBase64String);
    }

    @Test
    public void testBase64ToString_InputWithTrailingZeros() {
        byte[] byteArray = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
        String expectedBase64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+=";
        String resultBase64String = Base64.byteArrayToBase64(byteArray);
        assertEquals(expectedBase64String, resultBase64String);
    }

    @Test
    public void testBase64ToString_EmptyInput() {
        byte[] byteArray = new byte[0];
        String resultBase64String = Base64.byteArrayToBase64(byteArray);
        assertEquals("", resultBase64String);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToString_InvalidInput() {
        byte[] byteArray = new byte[] { 0, 1, 2, 3 }; // Input length should be a multiple of 3.
        Base64.byteArrayToBase64(byteArray);
    }

    // Test _$23183 method

    @Test
    public void test_$23183_ValidInput() {
        for (int i = 0; i < Base64._$23169.length; ++i) {
            char c = (char) Base64._$23169[i];
            int result = Base64._$23183(c, Base64._$23169);
            int expected = (byte) Base64._$23169[i];
            assertEquals(expected, result);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_$23183_InvalidInput() {
        char c = '!';
        assertThrows(IllegalArgumentException.class, () -> Base64._$23183(c, Base64._$23169));
    }
}