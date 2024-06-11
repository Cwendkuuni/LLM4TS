package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = new byte[] { 1, 2, 3, 4 };
        String input = "AQIDBA=="; // Base64 encoded representation of expected byte array
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidInput() {
        String input = "invalid";
        try {
            Base64.altBase64ToByteArray(input);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = new byte[] { 1, 2, 3, 4 };
        String input = "AQIDBA=="; // Base64 encoded representation of expected byte array
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArrayWithInvalidInput() {
        String input = "invalid";
        try {
            Base64.base64ToByteArray(input);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[] { 1, 2, 3, 4 };
        String expected = "AQIDBA==";
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = new byte[] { 1, 2, 3, 4 };
        String expected = "AQIDBA==";
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64WithPadding() {
        byte[] input = new byte[] { 1, 2, 3 };
        String expected = "AQIDBA==";
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64WithoutPadding() {
        byte[] input = new byte[] { 1, 2, 3 };
        String expected = "AQIDBA";
        String result = Base64.byteArrayToBase64(input, false);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64WithInvalidInput() {
        byte[] input = new byte[] { 1, 2, 3 };
        try {
            Base64.byteArrayToBase64(null, false);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testByteArrayToBase64WithNegativeIndex() {
        byte[] input = new byte[] { 1, 2, 3, 4 };
        try {
            Base64.byteArrayToBase64(input, -1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testByteArrayToBase64WithInvalidLength() {
        byte[] input = new byte[] { 1, 2, 3 };
        String expected = "AQIDBA";
        String result = Base64.byteArrayToBase64(input, 4);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64WithInvalidInput() {
        byte[] input = new byte[] { 1, 2, 3 };
        try {
            Base64.byteArrayToAltBase64(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testByteArrayToAltBase64WithNegativeIndex() {
        byte[] input = new byte[] { 1, 2, 3, 4 };
        try {
            Base64.byteArrayToAltBase64(input, -1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testByteArrayToAltBase64WithInvalidLength() {
        byte[] input = new byte[] { 1, 2, 3, 4 };
        try {
            Base64.byteArrayToAltBase64(input, 4);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }
}