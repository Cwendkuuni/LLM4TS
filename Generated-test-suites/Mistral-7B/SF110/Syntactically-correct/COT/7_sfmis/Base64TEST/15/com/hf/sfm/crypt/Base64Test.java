package com.hf.sfm.crypt.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.hf.sfm.crypt.Base64;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray_validInput() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedOutput = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32 };
        byte[] output = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testAltBase64ToByteArray_invalidInput_stringLengthNotMultipleOfFour() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/AB"; // length: 46

        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testAltBase64ToByteArray_invalidInput_illegalCharacter() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/X"; // contains 'X' which is an illegal character

        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testAltBase64ToByteArray_invalidInput_nullInput() {
        assertNull(Base64.altBase64ToByteArray(null));
    }

    @Test
    public void testBase64ToByteArray_validInput() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedOutput = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32 };
        byte[] output = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testBase64ToByteArray_invalidInput_stringLengthNotMultipleOfThree() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/AB"; // length: 46

        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray_invalidInput_illegalCharacter() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/X"; // contains 'X' which is an illegal character

        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray_invalidInput_nullInput() {
        assertNull(Base64.base64ToByteArray(null));
    }

    @Test
    public void testByteArrayToAltBase64_validInput() {
        byte[] input = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32 };
        String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String output = Base64.byteArrayToAltBase64(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64_validInput() {
        byte[] input = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32 };
        String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String output = Base64.byteArrayToBase64(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64_inputWithOnlyWhitespace() {
        byte[] input = new byte[0];
        String output = Base64.byteArrayToBase64(input);
        assertEquals("", output);
    }

    @Test
    public void testByteArrayToBase64_inputWithSingleByte() {
        byte[] input = new byte[] { 1 };
        String output = Base64.byteArrayToBase64(input);
        assertEquals("A", output);
    }
}