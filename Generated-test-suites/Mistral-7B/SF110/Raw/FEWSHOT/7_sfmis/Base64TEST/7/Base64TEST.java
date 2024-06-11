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
        String input = "ABCD";
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);

        input = "ABCD==";
        result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);

        input = "ABCD--";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));

        input = "ABCDABCD";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));

        input = "";
        assertNull(Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = new byte[] { 1, 2, 3, 4 };
        String input = "AQIDBA==";
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);

        input = "AQIDBA===";
        result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);

        input = "AQIDBA==-";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));

        input = "AQIDBA==AB";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));

        input = "";
        assertNull(Base64.base64ToByteArray(input));
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[] { 1, 2, 3, 4 };
        String expected = "ABCD";
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);

        input = new byte[] { 1, 2, 3, 4, 5 };
        expected += "==";
        result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);

        input = new byte[] { 1, 2, 3 };
        expected = "ABC";
        result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);

        input = new byte[] {};
        assertNull(Base64.byteArrayToAltBase64(input));
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = new byte[] { 1, 2, 3, 4 };
        String expected = "AQIDBA==";
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);

        input = new byte[] { 1, 2, 3, 4, 5 };
        expected += "=";
        result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);

        input = new byte[] { 1, 2, 3 };
        expected = "AQIDBA==";
        result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);

        input = new byte[] {};
        assertNull(Base64.byteArrayToBase64(input));
    }
}