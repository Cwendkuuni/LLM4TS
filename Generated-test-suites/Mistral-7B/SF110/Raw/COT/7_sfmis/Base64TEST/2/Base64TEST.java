package com.hf.sfm.crypt.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.junit.Test;

import com.hf.sfm.crypt.Base64;

/**
 * JUnit test file for Base64 class
 */
public class Base64Test {

    // Test altBase64ToByteArray method

    @Test
    public void testAltBase64ToByteArray_validInput() {
        String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/=";
        byte[] expected = { 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 52, 53, 54, 55, 56, 57, 58 };
        byte[] result = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArray_invalidInputLength() {
        String s = "abc";
        assertNull(Base64.altBase64ToByteArray(s));
    }

    @Test
    public void testAltBase64ToByteArray_invalidInputCharacter() {
        String s = "abcdefg@";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(s));
    }

    // Test base64ToByteArray method

    @Test
    public void testBase64ToByteArray_validInput() {
        String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/=";
        byte[] expected = { 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 52, 53, 54, 55, 56, 57, 58 };
        byte[] result = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray_invalidInputLength() {
        String s = "abc";
        assertNull(Base64.base64ToByteArray(s));
    }

    @Test
    public void testBase64ToByteArray_invalidInputCharacter() {
        String s = "abcdefg@";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(s));
    }

    // Test altBase64ToString method

    @Test
    public void testByteArrayToAltBase64_validInput() {
        byte[] bb = { 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 52, 53, 54, 55, 56, 57, 58 };
        String expected = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/=";
        String result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64_truncatedInput() {
        byte[] bb = { 97, 98, 99 };
        String result = Base64.byteArrayToAltBase64(bb);
        assertEquals("", result);
    }

    // Test base64ToString method

    @Test
    public void testByteArrayToBase64_validInput() {
        byte[] bb = { 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 52, 53, 54, 55, 56, 57, 58 };
        String expected = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/=";
        String result = Base64.byteArrayToBase64(bb);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_truncatedInput() {
        byte[] bb = { 97, 98, 99 };
        String result = Base64.byteArrayToBase64(bb);
        assertEquals("", result);
    }

}