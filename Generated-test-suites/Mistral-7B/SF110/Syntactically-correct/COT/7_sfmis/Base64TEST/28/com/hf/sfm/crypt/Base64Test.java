package com.hf.sfm.crypt.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.hf.sfm.crypt.Base64;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedBytes = Base64._$23169;
        byte[] actualBytes = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidLength() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidCharacter() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/x";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedBytes = Base64._$23169;
        byte[] actualBytes = Base64.byteArrayToBase64(expectedBytes);
        byte[] testBytes = Base64.base64ToByteArray(actualBytes);
        assertArrayEquals(expectedBytes, testBytes);
    }

    @Test
    public void testBase64ToByteArrayWithNullInput() {
        assertThrows(NullPointerException.class, () -> Base64.base64ToByteArray(null));
    }

    @Test
    public void testBase64ToByteArrayWithEmptyInput() {
        String input = "";
        byte[] actualBytes = Base64.base64ToByteArray(input);
        assertEquals(0, actualBytes.length);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = Base64._$23169;
        String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String actualOutput = Base64.byteArrayToAltBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = Base64._$23169;
        String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String actualOutput = Base64.byteArrayToBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testByteArrayToBase64WithNullInput() {
        assertThrows(NullPointerException.class, () -> Base64.byteArrayToBase64(null));
    }

    @Test
    public void testByteArrayToBase64WithEmptyInput() {
        byte[] input = new byte[0];
        String actualOutput = Base64.byteArrayToBase64(input);
        assertEquals("", actualOutput);
    }
}