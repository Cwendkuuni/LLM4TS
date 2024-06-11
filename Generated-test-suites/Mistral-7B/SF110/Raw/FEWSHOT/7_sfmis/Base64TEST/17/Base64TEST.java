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
        String input = "_A_B_C_D";
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidLength() {
        String input = "_A_B_C_";
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
    public void testBase64ToByteArrayWithInvalidLength() {
        String input = "AQIDBA=";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
    }

    @Test
    public void testByteArrayToAltBase64() {
        String expected = "_A_B_C_D";
        byte[] input = new byte[] { 1, 2, 3, 4 };
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64() {
        String expected = "AQIDBA==";
        byte[] input = new byte[] { 1, 2, 3, 4 };
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64WithSpecialChars() {
        String expected = "SGVsbG8gV29ybGQh";
        byte[] input = new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03, '\u0040' };
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArrayWithSpecialChars() {
        byte[] expected = new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03, '\u0040' };
        String input = "SGVsbG8gV29ybGQh";
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testInvalidBase64Input() {
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(new byte[0]));
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(new byte[] { -1 }));
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(new byte[] { 0, 0 }));
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(new byte[] { 0, 0, 0 }));
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(""));
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray("ABC"));
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray("ABCABC"));
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray("ABCABCABC"));
    }

    @Test
    public void testMainMethod() {
        String input = "0123456789";
        byte[] bytes = input.getBytes();
        String encoded = Base64.byteArrayToBase64(bytes);
        byte[] decodedBytes = Base64.base64ToByteArray(encoded);
        assertEquals(input, new String(decodedBytes));
    }
}