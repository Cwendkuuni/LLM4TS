package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expectedBytes = new byte[] { 35, 62, 70, 65, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        String altBase64String = "ZXhhbXBsZSBuYW1lIGlzIGxpa2U=";
        byte[] resultBytes = Base64.altBase64ToByteArray(altBase64String);
        assertArrayEquals(expectedBytes, resultBytes);

        // Test with an empty string
        String emptyAltBase64String = "";
        resultBytes = Base64.altBase64ToByteArray(emptyAltBase64String);
        assertNull(resultBytes);

        // Test with an invalid string
        String invalidAltBase64String = "ZXhhbXBsZSBuYW1lIGlzIGxpa2D=";
        try {
            Base64.altBase64ToByteArray(invalidAltBase64String);
            assert false : "IllegalArgumentException expected";
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expectedBytes = new byte[] { 35, 62, 70, 65, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        String base64String = "ZXhhbXBsZSBuYW1lIHNlbGVz";
        byte[] resultBytes = Base64.base64ToByteArray(base64String);
        assertArrayEquals(expectedBytes, resultBytes);

        // Test with an empty string
        String emptyBase64String = "";
        resultBytes = Base64.base64ToByteArray(emptyBase64String);
        assertNull(resultBytes);

        // Test with a string containing only whitespace
        String whitespaceBase64String = "   ";
        try {
            Base64.base64ToByteArray(whitespaceBase64String);
            assert false : "IllegalArgumentException expected";
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        String expectedAltBase64String = "ZXhhbXBsZSBuYW1lIGlzIGxpa2U=";
        byte[] inputBytes = new byte[] { 35, 62, 70, 65, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        String resultAltBase64String = Base64.byteArrayToAltBase64(inputBytes);
        assertEquals(expectedAltBase64String, resultAltBase64String);

        // Test with an empty byte array
        byte[] emptyInputBytes = new byte[0];
        resultAltBase64String = Base64.byteArrayToAltBase64(emptyInputBytes);
        assertEquals("", resultAltBase64String);
    }

    @Test
    public void testByteArrayToBase64() {
        String expectedBase64String = "ZXhhbXBsZSBuYW1lIHNlbGVz";
        byte[] inputBytes = new byte[] { 35, 62, 70, 65, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        String resultBase64String = Base64.byteArrayToBase64(inputBytes);
        assertEquals(expectedBase64String, resultBase64String);

        // Test with an empty byte array
        byte[] emptyInputBytes = new byte[0];
        resultBase64String = Base64.byteArrayToBase64(emptyInputBytes);
        assertEquals("", resultBase64String);
    }
}