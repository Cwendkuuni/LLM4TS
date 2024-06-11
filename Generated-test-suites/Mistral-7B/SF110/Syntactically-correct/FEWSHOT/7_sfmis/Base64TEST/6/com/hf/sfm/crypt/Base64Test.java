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

        input = "AQIDBA===";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));

        input = "test";
        assertNull(Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = new byte[] { 1, 2, 3, 4 };
        String input = "AQIDBA==";
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);

        input = "AQIDBA===";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));

        input = "test";
        assertNull(Base64.base64ToByteArray(input));
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[] { 1, 2, 3, 4 };
        String expected = "QEDnZWw=";
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);

        input = new byte[] { 1, 2 };
        result = Base64.byteArrayToAltBase64(input);
        assertEquals("QDQ=", result);

        result = Base64.byteArrayToAltBase64(new byte[0][0]);
        assertNull(result);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = new byte[] { 1, 2, 3, 4 };
        String expected = "AQIDBA==";
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);

        input = new byte[] { 1, 2 };
        result = Base64.byteArrayToBase64(input);
        assertEquals("AQIDBA==", result);

        input = new byte[0][0];
        result = Base64.byteArrayToBase64(input);
        assertNull(result);
    }
}