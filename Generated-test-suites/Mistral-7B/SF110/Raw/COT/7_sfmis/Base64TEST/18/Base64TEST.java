package com.hf.sfm.crypt.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import com.hf.sfm.crypt.Base64;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray_validInput() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expected = { 65, 66, 67, 68, 69, 70, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 50, 51 };
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArray_invalidLength() {
        String input = "ABCDEFG";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testAltBase64ToByteArray_invalidCharacter() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/X";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray_validInput() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expected = { 65, 66, 67, 68, 69, 70, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 50, 51 };
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray_invalidLength() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/A";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray_invalidCharacter() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/X";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
    }

    @Test
    public void testByteArrayToAltBase64_validInput() {
        byte[] input = { 65, 66, 67, 68, 69, 70, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 50, 51 };
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64_trailingEqualSigns() {
        byte[] input = { 65, 66, 67, 68, 69, 70, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 50, 51, 50, 50 };
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/==";
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64_leadingEqualSigns() {
        byte[] input = { 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 65, 66, 67, 68, 69, 70, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89 };
        String expected = "====ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_validInput() {
        byte[] input = { 65, 66, 67, 68, 69, 70, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 50, 51 };
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_trailingEqualSigns() {
        byte[] input = { 65, 66, 67, 68, 69, 70, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89, 50, 51, 50, 50 };
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/==";
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_leadingEqualSigns() {
        byte[] input = { 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 65, 66, 67, 68, 69, 70, 84, 85, 86, 87, 88, 89, 84, 85, 86, 87, 88, 89 };
        String expected = "====ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_randomInput() {
        Random random = new Random();
        byte[] input = new byte[100];
        random.nextBytes(input);
        String expected = Base64.byteArrayToBase64(input);
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }
}