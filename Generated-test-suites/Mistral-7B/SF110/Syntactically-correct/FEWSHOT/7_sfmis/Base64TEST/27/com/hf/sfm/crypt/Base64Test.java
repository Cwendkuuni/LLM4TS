package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = new byte[] { 1, 2, 3, 4 };
        String input = "AQIDBA==";
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArrayInvalidLength() {
        String input = "AQIDBA===";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = new byte[] { 1, 2, 3, 4 };
        String input = "AQIDBA==";
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArrayInvalidLength() {
        String input = "AQIDBA===";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
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
    public void testByteArrayToBase64InvalidByte() {
        byte[] input = { (byte) 128 };
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(input));
    }

    @Test
    public void testBase64ToByteArrayInvalidCharacter() {
        String input = "ABC==x";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArrayNullInput() {
        assertNull(Base64.base64ToByteArray(null));
    }

    @Test
    public void testByteArrayToAltBase64NullInput() {
        assertNull(Base64.byteArrayToAltBase64(null));
    }

    @Test
    public void testBase64Main() {
        String input = "0123456789";
        byte[] byteArray = input.getBytes();
        String base64 = Base64.byteArrayToBase64(byteArray);
        System.out.println(base64);
        byte[] decodedByteArray = Base64.base64ToByteArray(base64);
        System.out.println(new String(decodedByteArray));
    }
}