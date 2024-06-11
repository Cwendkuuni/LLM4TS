package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        String base64String = new String(expected, 0, expected.length);
        byte[] result = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidLength() {
        String base64String = "ABCDEFG";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(base64String));
    }

    @Test
    public void testAltBase64ToByteArrayWithTrailingEquals() {
        String base64String = "ABCDEF==";
        byte[] result = Base64.altBase64ToByteArray(base64String);
        assertNull(result);
    }

    @Test
    public void testAltBase64ToByteArrayWithTrailingEqualsAndLengthOne() {
        String base64String = "A=";
        byte[] result = Base64.altBase64ToByteArray(base64String);
        assertNull(result);
    }

    @Test
    public void testAltBase64ToByteArrayWithTrailingEqualsAndLengthTwo() {
        String base64String = "AA=";
        byte[] result = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(new byte[] { 0 }, result);
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        String base64String = new String(expected, 0, expected.length);
        byte[] result = Base64.base64ToByteArray(base64String);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArrayWithInvalidLength() {
        String base64String = "ABCDEFG";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(base64String));
    }

    @Test
    public void testBase64ToByteArrayWithTrailingEquals() {
        String base64String = "ABCDEF==";
        byte[] result = Base64.base64ToByteArray(base64String);
        assertNull(result);
    }

    @Test
    public void testBase64ToByteArrayWithTrailingEqualsAndLengthOne() {
        String base64String = "A=";
        byte[] result = Base64.base64ToByteArray(base64String);
        assertNull(result);
    }

    @Test
    public void testBase64ToByteArrayWithTrailingEqualsAndLengthTwo() {
        String base64String = "AA=";
        byte[] result = Base64.base64ToByteArray(base64String);
        assertArrayEquals(new byte[] { 0 }, result);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        String output = Base64.byteArrayToAltBase64(input);
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", output);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        String output = Base64.byteArrayToBase64(input);
        assertEquals("AQIDBA==", output);
    }

    @Test
    public void testByteArrayToBase64WithNegativeInput() {
        byte[] input = new byte[] { -1, -2, -3, -4, -5 };
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(input));
    }

    @Test
    public void testByteArrayToBase64WithEmptyInput() {
        byte[] input = new byte[] {};
        String output = Base64.byteArrayToBase64(input);
        assertEquals("", output);
    }

    @Test
    public void testByteArrayToBase64WithSingleByteInput() {
        byte[] input = new byte[] { 0 };
        String output = Base64.byteArrayToBase64(input);
        assertEquals("AQ==", output);
    }
}