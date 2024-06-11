package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = new byte[]{1, 2, 3, 4};
        String input = "_ABCD";
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidLength() {
        String input = "_ABCD_";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testAltBase64ToByteArrayWithTrailingEquals() {
        String input = "_ABCD__";
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(new byte[]{1, 2, 3, 4}, result);
    }

    @Test
    public void testAltBase64ToByteArrayWithTrailingEqualsOnly() {
        String input = "____";
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    public void testAltBase64ToByteArrayWithNullInput() {
        assertNull(Base64.altBase64ToByteArray(null));
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = new byte[]{1, 2, 3, 4};
        String input = "AQIDBA==";
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArrayWithInvalidLength() {
        String input = "AQIDBA===";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArrayWithTrailingEquals() {
        String input = "AQIDBA===";
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(new byte[]{1, 2, 3, 4}, result);
    }

    @Test
    public void testBase64ToByteArrayWithTrailingEqualsOnly() {
        String input = "====";
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    public void testBase64ToByteArrayWithNullInput() {
        assertNull(Base64.base64ToByteArray(null));
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[]{1, 2, 3, 4};
        String expected = "_ABCD";
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = new byte[]{1, 2, 3, 4};
        String expected = "AQIDBA==";
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64WithInvalidByte() {
        byte[] input = new byte[]{-1};
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(input));
    }

    @Test
    public void testByteArrayToBase64WithNullInput() {
        assertNull(Base64.byteArrayToBase64(null));
    }
}