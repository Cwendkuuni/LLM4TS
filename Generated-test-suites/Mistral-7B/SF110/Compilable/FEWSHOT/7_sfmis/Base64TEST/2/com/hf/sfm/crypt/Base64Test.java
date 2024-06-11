package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBA==");
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayWithInvalidLength() {
        Base64.altBase64ToByteArray("AQIDBA===");
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        byte[] actual = Base64.base64ToByteArray("AQIDBA==");
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayWithInvalidLength() {
        Base64.base64ToByteArray("AQIDBA===");
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        String expected = "AQIDBA==";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        String expected = "AQIDBA==";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64WithPadding() {
        byte[] input = new byte[] { 0, 1, 2 };
        String expected = "AQID";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64WithPadding2() {
        byte[] input = new byte[] { 0, 1, 2, 3 };
        String expected = "AQIDBA==";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64WithPadding3() {
        byte[] input = new byte[] { 0, 1, 2, 3, 4 };
        String expected = "AQIDBA==";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testAltBase64ToByteArrayWithPadding() {
        byte[] expected = new byte[] { 0, 1, 2 };
        byte[] actual = Base64.altBase64ToByteArray("AQID");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAltBase64ToByteArrayWithPadding2() {
        byte[] expected = new byte[] { 0, 1, 2, 3 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBA==");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAltBase64ToByteArrayWithPadding3() {
        byte[] expected = new byte[] { 0, 1, 2, 3, 4 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBA==");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBase64ToByteArrayWithInvalidInput() {
        assertNull(Base64.base64ToByteArray(null));
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidInput2() {
        assertNull(Base64.altBase64ToByteArray(null));
    }
}