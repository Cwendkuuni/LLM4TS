package com.hf.sfm.crypt;

import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    

    // Test for altBase64ToByteArray method
    @Test
    public void testAltBase64ToByteArray_ValidInput() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedByteArray = new byte[28]; // 28 bytes for 24 encoding characters and 4 padding characters
        byte[] actualByteArray = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_InvalidInput_StringLengthMultipleOfFour() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789=";
        Base64.altBase64ToByteArray(base64String); // This line should throw an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_InvalidInput_StringLengthNotMultipleOfFour() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Base64.altBase64ToByteArray(base64String); // This line should throw an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_InvalidInput_IllegalCharacter() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!";
        Base64.altBase64ToByteArray(base64String); // This line should throw an IllegalArgumentException
    }

    // Test for base64ToByteArray method
    @Test
    public void testBase64ToByteArray_ValidInput() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedByteArray = new byte[28]; // 28 bytes for 24 encoding characters and 4 padding characters
        byte[] actualByteArray = Base64.base64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_InvalidInput_StringLengthMultipleOfFour() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789=";
        Base64.base64ToByteArray(base64String); // This line should throw an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_InvalidInput_StringLengthNotMultipleOfFour() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Base64.base64ToByteArray(base64String); // This line should throw an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_InvalidInput_IllegalCharacter() {
        String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!";
        Base64.base64ToByteArray(base64String); // This line should throw an IllegalArgumentException
    }

    // Test for altBase64ToBase64 method
    @Test
    public void testByteArrayToAltBase64_ValidInput() {
        byte[] byteArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
        String expectedBase64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String actualBase64String = Base64.byteArrayToAltBase64(byteArray);
        assertEquals(expectedBase64String, actualBase64String);
    }

    @Test
    public void testByteArrayToAltBase64_ValidInputWithPadding() {
        byte[] byteArray = {0, 1, 2, 3, 4, 5};
        String expectedBase64String = "MTIzNDU2Nzg5";
        String actualBase64String = Base64.byteArrayToAltBase64(byteArray);
        assertEquals(expectedBase64String, actualBase64String);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testByteArrayToAltBase64_InvalidInput() {
        byte[] byteArray = {0, 1, 2};
        Base64.byteArrayToAltBase64(byteArray); // This line should throw an IllegalArgumentException
    }

    // Test for base64ToBase64 method
    @Test
    public void testByteArrayToBase64_ValidInput() {
        byte[] byteArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
        String expectedBase64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String actualBase64String = Base64.byteArrayToBase64(byteArray);
        assertEquals(expectedBase64String, actualBase64String);
    }

    @Test
    public void testByteArrayToBase64_ValidInputWithPadding() {
        byte[] byteArray = {0, 1, 2, 3, 4, 5};
        String expectedBase64String = "MTIzNDU2Nzg5";
        String actualBase64String = Base64.byteArrayToBase64(byteArray);
        assertEquals(expectedBase64String, actualBase64String);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testByteArrayToBase64_InvalidInput() {
        byte[] byteArray = {0, 1, 2};
        Base64.byteArrayToBase64(byteArray); // This line should throw an IllegalArgumentException
    }

    
}