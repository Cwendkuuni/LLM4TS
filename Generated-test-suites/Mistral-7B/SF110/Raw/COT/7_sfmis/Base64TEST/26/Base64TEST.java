package com.hf.sfm.crypt.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.hf.sfm.crypt.Base64;

public class Base64Test {

    // Test method: altBase64ToByteArray
    
    @Test
    public void testAltBase64ToByteArray_ValidInput() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedByteArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 };
        byte[] resultByteArray = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, resultByteArray);
    }

    @Test
    public void testAltBase64ToByteArray_IllegalArgumentException_InputLengthNotMultipleOfFour() {
        String base64String = "ABC";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(base64String));
    }

    // Test method: base64ToByteArray

    @Test
    public void testBase64ToByteArray_ValidInput() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedByteArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 };
        byte[] resultByteArray = Base64.base64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, resultByteArray);
    }

    // Test method: base64ToByteArray_IllegalArgumentException_InputLengthNotMultipleOfThree()

    @Test
    public void testBase64ToByteArray_IllegalArgumentException_InputLengthNotMultipleOfThree() {
        String base64String = "ABCDEF";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(base64String));
    }

    // Test method: _$23183

    @Test
    public void test_$23183_ValidInput() {
        char character = 'A';
        byte expectedByte = (byte) 0;
        byte resultByte = Base64._$23183(character, Base64._$23169);
        assertEquals(expectedByte, resultByte);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_$23183_IllegalArgumentException_InvalidCharacter() {
        char character = '~';
        Base64._$23183(character, Base64._$23169);
    }

    // Test method: altBase64ToByteArray

    @Test
    public void testAltBase64ToByteArray_PaddingCharacters() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+";
        byte[] expectedByteArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 };
        byte[] resultByteArray = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, resultByteArray);
    }

    // Test method: byteArrayToAltBase64

    @Test
    public void testByteArrayToAltBase64_ValidInput() {
        byte[] byteArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 };
        String expectedBase64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+";
        String resultBase64String = Base64.byteArrayToAltBase64(byteArray);
        assertEquals(expectedBase64String, resultBase64String);
    }

    // Test method: byteArrayToBase64

    @Test
    public void testByteArrayToBase64_ValidInput() {
        byte[] byteArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 };
        String expectedBase64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+";
        String resultBase64String = Base64.byteArrayToBase64(byteArray);
        assertEquals(expectedBase64String, resultBase64String);
    }

    // Test method: byteArrayToBase64_PaddingCharacters

    @Test
    public void testByteArrayToBase64_PaddingCharacters() {
        byte[] byteArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
        String expectedBase64String = "ABCDEFGHIJKLMNOPQRST";
        String resultBase64String = Base64.byteArrayToBase64(byteArray);
        assertEquals(expectedBase64String, resultBase64String);
    }
}