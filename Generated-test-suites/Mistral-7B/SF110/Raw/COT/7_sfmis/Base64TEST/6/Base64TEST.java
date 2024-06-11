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
        String input = "RGV2aXNpbGllcyw=";
        byte[] expected = { 72, 101, 86, 103, 101, 116, 116, 108, 101, 99, 107, 105, 110, 111, 116, 116, 103, 101, 99, 116, 116, 108, 101, 116, 116, 108, 101, 115, 116, 115, 111, 110, 32, 115, 115, 116, 111, 110, 105, 99, 111, 110, 115, 32, 108, 101, 110, 111, 32, 116, 111, 110, 111, 116, 111, 116, 111, 117, 115, 104 };
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArray_invalidLength() {
        String input = "RGV2aXNpbGllcyw=="; // missing '=' character for proper length
        assertNull(Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testAltBase64ToByteArray_invalidInput() {
        String input = "RGV2aXNpbGllcywX"; // invalid character in input
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testAltBase64ToByteArray_emptyInput() {
        String input = "";
        assertNull(Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray_validInput() {
        String input = "SGVsbG8sIFdvcmxkIQ==";
        byte[] expected = { 83, 76, 67, 82, 69, 70, 65, 82, 69, 67, 72, 84, 65, 78, 74, 68, 65, 81, 69, 62, 65, 84, 63, 6F, 6D };
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray_emptyInput() {
        String input = "";
        assertNull(Base64.base64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray_invalidInput() {
        String input = "SGVsbG8sIFdvcmxkIQ==X"; // invalid character in input
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
    }

    @Test
    public void testByteArrayToAltBase64_validInput() {
        byte[] input = { 72, 101, 86, 103, 101, 116, 116, 108, 101, 99, 107, 105, 110, 111, 116, 116, 103, 101, 99, 116, 116, 108, 101, 116, 116, 108, 101, 115, 116, 115, 111, 110, 32, 115, 115, 116, 111, 110, 105, 99, 111, 110, 115, 32, 108, 101, 110, 111, 32, 116, 111, 110, 111, 116, 111, 116, 111, 117, 115, 104 };
        String expected = "RGV2aXNpbGllcyw=";
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64_emptyInput() {
        byte[] input = {};
        assertNull(Base64.byteArrayToAltBase64(input));
    }

    @Test
    public void testByteArrayToBase64_validInput() {
        byte[] input = { 83, 76, 67, 82, 69, 70, 65, 82, 69, 67, 72, 84, 65, 78, 74, 68, 65, 81, 69, 62, 65, 84, 63, 6F, 6D };
        String expected = "SGVsbG8sIFdvcmxkIQ==";
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_emptyInput() {
        byte[] input = {};
        assertNull(Base64.byteArrayToBase64(input));
    }

    @Test
    public void testByteArrayToBase64_validPadding() {
        byte[] input = { 83, 76, 67, 82, 69, 70, 65, 82, 69, 67, 72, 84, 65, 78, 74, 68, 65, 81, 69, 62, 65, 84, 63 }; // missing '=' character for proper padding
        String expected = "SGVsbG8sIFdvcmxkIQ==";
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }

}